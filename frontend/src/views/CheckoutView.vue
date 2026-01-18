<script setup>
import { computed, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useCart } from '../composables/useCart'

const { cartItems, subtotal, clearCart } = useCart()
const { authorizedRequest } = useAuth()

const shipping = 9.5
const taxesRate = 0.08

const form = ref({
  fullName: '',
  email: '',
  address: '',
  city: '',
  state: '',
  postal: '',
  payment: 'card',
})

const placed = ref(false)
const placing = ref(false)

const total = computed(() => {
  const taxes = subtotal.value * taxesRate
  const shippingCost = cartItems.value.length ? shipping : 0
  return subtotal.value + taxes + shippingCost
})

const placeOrder = () => {
  placed.value = true
  clearCart()
}
</script>

<template>
  <section class="checkout">
    <div>
      <p class="pill">Checkout</p>
      <h2>Secure your shipment</h2>
      <p class="lede">Confirm delivery details and we will ice-pack your selection.</p>

      <div v-if="cartItems.length === 0 && !placed" class="empty">
        <p>Your cart is empty. Add items to continue.</p>
        <RouterLink class="btn btn--primary" to="/">Return to shop</RouterLink>
      </div>

      <form v-else class="panel form" @submit.prevent="placeOrder">
        <div class="panel__header">
          <p class="panel__title">Delivery & payment</p>
          <p class="panel__copy">We ship insulated with gel packs and proof of temperature.</p>
        </div>

        <div class="field">
          <label>Full name</label>
          <input v-model="form.fullName" type="text" required />
        </div>
        <div class="field">
          <label>Email</label>
          <input v-model="form.email" type="email" required />
        </div>
        <div class="field">
          <label>Street address</label>
          <input v-model="form.address" type="text" required />
        </div>
        <div class="field two-col">
          <div>
            <label>City</label>
            <input v-model="form.city" type="text" required />
          </div>
          <div>
            <label>State</label>
            <input v-model="form.state" type="text" required />
          </div>
        </div>
        <div class="field">
          <label>Postal code</label>
          <input v-model="form.postal" type="text" required />
        </div>

        <div class="field">
          <label>Payment</label>
          <div class="choices">
            <label>
              <input v-model="form.payment" type="radio" value="card" />
              Card
            </label>
            <label>
              <input v-model="form.payment" type="radio" value="ach" />
              ACH
            </label>
            <label>
              <input v-model="form.payment" type="radio" value="cod" />
              COD
            </label>
          </div>
        </div>

        <button class="btn btn--primary" type="submit" :disabled="placing">Place order</button>
        <p v-if="placing" class="placing">Placing order...</p>
        <p v-if="placed" class="success">
          Order placed! You will receive confirmation and tracking soon.
        </p>
      </form>
    </div>

    <aside class="panel summary">
      <div class="panel__header">
        <p class="panel__title">Review box</p>
        <p class="panel__copy">Traceable sourcing, packed within 24 hours.</p>
      </div>

      <div class="summary__items">
        <div v-for="item in cartItems" :key="item.id" class="summary__item">
          <div>
            <p class="summary__name">{{ item.name }}</p>
            <p class="summary__meta">{{ item.quantity }} x ${{ item.price }}</p>
          </div>
          <p class="summary__price">${{ (item.price * item.quantity).toFixed(2) }}</p>
        </div>
        <p v-if="!cartItems.length" class="summary__empty">No items yet.</p>
      </div>

      <div class="summary-row">
        <span>Subtotal</span>
        <strong>${{ subtotal.toFixed(2) }}</strong>
      </div>
      <div class="summary-row">
        <span>Taxes (8%)</span>
        <strong>${{ (subtotal * taxesRate).toFixed(2) }}</strong>
      </div>
      <div class="summary-row">
        <span>Shipping</span>
        <strong>{{ cartItems.length ? `$${shipping.toFixed(2)}` : 'Free' }}</strong>
      </div>
      <div class="summary-row summary-row--total">
        <span>Total due</span>
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

.empty {
  margin-top: 12px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 14px;
  padding: 14px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.panel {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 16px;
  padding: 16px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.06);
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

.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  color: #d7e5ff;
}

.field input {
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.04);
  color: var(--night-text);
}

.two-col {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.choices {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.choices input {
  margin-right: 6px;
}

.success {
  color: #63e6be;
  margin: 0;
  font-weight: 700;
}

.placing {
  color: #d7e5ff;
  margin: 0;
  font-weight: 700;
}

.summary {
  position: sticky;
  top: 8px;
  align-self: start;
}

.summary__items {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 12px;
}

.summary__item {
  display: flex;
  justify-content: space-between;
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 12px;
  padding: 10px;
}

.summary__name {
  margin: 0 0 4px;
  font-weight: 700;
}

.summary__meta {
  margin: 0;
  color: var(--night-muted);
}

.summary__price {
  margin: 0;
  font-weight: 700;
}

.summary__empty {
  margin: 0;
  color: var(--night-muted);
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
  color: #d7e5ff;
}

.summary-row--total {
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  padding-top: 10px;
  margin-top: 12px;
  font-size: 18px;
}
</style>
