<script setup>
import { computed, ref } from 'vue'
import { useCart } from '../composables/useCart'
import { useAuth } from '../composables/useAuth'

const { cartItems, subtotal, clearCart } = useCart()
const { isAuthenticated, authUser, authorizedRequest } = useAuth()

const notes = ref('')
const saveInfo = ref(false)
const receiptEmail = ref(authUser.value?.email || '')
const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const shipping = computed(() => (subtotal.value > 0 ? 9.5 : 0))
const total = computed(() => subtotal.value + shipping.value)

const submitOrder = async () => {
  errorMessage.value = ''
  successMessage.value = ''

  if (!isAuthenticated.value) {
    errorMessage.value = 'Please sign in to complete checkout.'
    return
  }

  if (!cartItems.value.length) {
    errorMessage.value = 'Your cart is empty.'
    return
  }

  const receipt = {
    items: cartItems.value,
    subtotal: subtotal.value,
    shipping: shipping.value,
    total: total.value,
  }

  isSubmitting.value = true
  try {
    await authorizedRequest({
      method: 'POST',
      url: '/api/orders',
      data: {
        description: notes.value || 'Fish Market order',
        receipt,
        saveInfo: saveInfo.value,
        receiptEmail: receiptEmail.value,
      },
    })
    clearCart()
    successMessage.value = 'Order placed. Your receipt is on the way.'
  } catch (err) {
    errorMessage.value = 'Unable to place order right now.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <section class="checkout">
    <div class="checkout__form">
      <p class="pill">Checkout</p>
      <h2>Finish your dockside pickup</h2>
      <p class="lede">Confirm your order, then we will prep your catch for same-day pickup.</p>

      <div class="card">
        <h3>Contact</h3>
        <label>
          Name
          <input type="text" :value="authUser?.name || ''" disabled />
        </label>
        <label>
          Email for receipt
          <input v-model="receiptEmail" type="email" placeholder="dock@fishmarket.co" />
        </label>
        <label>
          Order notes
          <textarea v-model="notes" rows="3" placeholder="Pickup notes or prep requests"></textarea>
        </label>
        <label class="checkbox-label">
          <input v-model="saveInfo" type="checkbox" />
          Save my contact info for next time
        </label>
      </div>

      <div class="card">
        <h3>Payment</h3>
        <p class="muted">
          Payments are captured securely at pickup. You will see a confirmation once the order is
          placed.
        </p>
      </div>

      <div v-if="errorMessage" class="alert alert--error">{{ errorMessage }}</div>
      <div v-if="successMessage" class="alert alert--success">{{ successMessage }}</div>

      <button class="btn btn--primary" :disabled="isSubmitting" @click="submitOrder">
        {{ isSubmitting ? 'Placing order…' : 'Place order' }}
      </button>
    </div>

    <aside class="panel">
      <div class="panel__header">
        <p class="panel__title">Order summary</p>
        <p class="panel__copy">Review your seafood and gear items.</p>
      </div>

      <div v-if="!cartItems.length" class="empty">
        <p>No items in your cart yet.</p>
      </div>

      <div v-else class="summary-items">
        <article v-for="item in cartItems" :key="item.id" class="summary-item">
          <div>
            <p class="summary-item__name">{{ item.name }}</p>
            <p class="summary-item__meta">{{ item.quantity }} × ${{ item.price }}</p>
          </div>
          <strong>${{ (item.price * item.quantity).toFixed(2) }}</strong>
        </article>
      </div>

      <div class="summary-row">
        <span>Subtotal</span>
        <strong>${{ subtotal.toFixed(2) }}</strong>
      </div>
      <div class="summary-row">
        <span>Cold-pack shipping</span>
        <strong>{{ shipping > 0 ? `$${shipping.toFixed(2)}` : 'Free' }}</strong>
      </div>
      <div class="summary-row summary-row--total">
        <span>Total</span>
        <strong>${{ total.toFixed(2) }}</strong>
      </div>
    </aside>
  </section>
</template>

<style scoped>
.checkout {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 18px;
  align-items: start;
}

.pill {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(0, 255, 209, 0.12);
  color: var(--night-accent);
  font-weight: 700;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.lede {
  color: var(--night-muted);
}

.card {
  margin-top: 12px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 14px;
  padding: 14px;
  display: grid;
  gap: 10px;
}

.card h3 {
  margin: 0;
}

label {
  display: grid;
  gap: 6px;
  font-size: 14px;
}

input,
textarea {
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.04);
  color: var(--night-text);
  padding: 8px 10px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-weight: normal;
}

.checkbox-label input {
  width: auto;
  margin: 0;
}

.panel {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 16px;
  padding: 16px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.06);
  position: sticky;
  top: 8px;
}

.panel__header {
  margin-bottom: 10px;
}

.panel__title {
  margin: 0 0 4px;
  font-size: 18px;
  font-weight: 700;
}

.panel__copy {
  margin: 0;
  color: var(--night-muted);
}

.summary-items {
  display: grid;
  gap: 8px;
  margin: 12px 0;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}

.summary-item__name {
  margin: 0;
  font-weight: 600;
}

.summary-item__meta {
  margin: 0;
  color: var(--night-muted);
  font-size: 13px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  color: #d7e5ff;
}

.summary-row--total {
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  padding-top: 10px;
  margin-top: 12px;
  font-size: 18px;
}

.alert {
  margin-top: 12px;
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid transparent;
  font-weight: 600;
}

.alert--error {
  background: rgba(255, 92, 92, 0.12);
  border-color: rgba(255, 92, 92, 0.4);
  color: #ff7b7b;
}

.alert--success {
  background: rgba(0, 255, 209, 0.12);
  border-color: rgba(0, 255, 209, 0.35);
  color: var(--night-accent);
}

.empty {
  margin: 12px 0;
  color: var(--night-muted);
}

.muted {
  color: var(--night-muted);
  margin: 0;
}
</style>
