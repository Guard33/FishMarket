<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useCart } from '../composables/useCart'

const { cartItems, updateQuantity, removeFromCart, subtotal, clearCart } = useCart()

const shipping = computed(() => (subtotal.value > 0 ? 9.5 : 0))
const total = computed(() => subtotal.value + shipping.value)
</script>

<template>
  <section class="cart">
    <div>
      <p class="pill">Cart</p>
      <h2>Your current box</h2>
      <p class="lede">Adjust portions, then slide into checkout. Cold packs included.</p>

      <div v-if="cartItems.length === 0" class="empty">
        <p>No fish yet. Browse the shop to build your box.</p>
        <RouterLink class="btn btn--primary" to="/">Browse shop</RouterLink>
      </div>

      <div v-else class="items">
        <article v-for="item in cartItems" :key="item.id" class="item">
          <div>
            <p class="item__eyebrow">{{ item.origin }}</p>
            <p class="item__name">{{ item.name }}</p>
            <p class="item__meta">{{ item.weight }} lb · {{ item.length }} in</p>
          </div>
          <div class="item__controls">
            <input
              :value="item.quantity"
              type="number"
              min="1"
              @input="updateQuantity(item.id, Number($event.target.value))"
            />
            <p class="item__price">${{ (item.price * item.quantity).toFixed(2) }}</p>
            <button class="link" @click="removeFromCart(item.id)">Remove</button>
          </div>
        </article>
        <button class="link" @click="clearCart">Clear cart</button>
      </div>
    </div>

    <aside class="panel">
      <div class="panel__header">
        <p class="panel__title">Order summary</p>
        <p class="panel__copy">Review totals before checkout.</p>
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

      <RouterLink v-if="cartItems.length" class="btn btn--primary" to="/checkout">
        Proceed to checkout
      </RouterLink>
      <RouterLink v-else class="btn btn--ghost" to="/">Add items</RouterLink>
    </aside>
  </section>
</template>

<style scoped>
.cart {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
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
  color: #9fb6d7;
  margin-bottom: 12px;
}

.empty {
  background: var(--night-surface);
  border: 1px solid var(--night-border);
  border-radius: 14px;
  padding: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.item {
  background: var(--night-surface);
  border: 1px solid var(--night-border);
  border-radius: 14px;
  padding: 12px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.item__eyebrow {
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #7bd6ff;
  font-size: 12px;
}

.item__name {
  margin: 4px 0;
  font-weight: 700;
}

.item__meta {
  margin: 0;
  color: #9fb6d7;
}

.item__controls {
  display: grid;
  grid-template-columns: 90px 1fr auto;
  align-items: center;
  gap: 8px;
}

.item__controls input {
  width: 80px;
  padding: 8px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.04);
  color: #e6edf7;
}

.item__price {
  margin: 0;
  font-weight: 700;
}

.panel {
  background: var(--night-surface);
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
  color: #9fb6d7;
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

.link {
  color: #8bd5ff;
  text-decoration: none;
  font-weight: 700;
  background: transparent;
  border: none;
  cursor: pointer;
}
</style>
