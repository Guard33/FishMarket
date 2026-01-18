<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '../composables/useAuth'

const { authorizedRequest } = useAuth()
const orders = ref([])
const loading = ref(true)

const fetchOrders = async () => {
  try {
    const { data } = await authorizedRequest({ method: 'GET', url: '/api/orders' })
    orders.value = data
  } catch (error) {
    console.error('Failed to fetch orders:', error)
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrders)

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString()
}

const parseReceipt = (receiptJson) => {
  try {
    return JSON.parse(receiptJson)
  } catch {
    return null
  }
}
</script>

<template>
  <section class="history">
    <div>
      <p class="pill">History</p>
      <h2>Your order history</h2>
      <p class="lede">View your past orders and receipts.</p>

      <div v-if="loading" class="loading">Loading orders...</div>

      <div v-else-if="orders.length === 0" class="empty">
        <p>You haven't placed any orders yet.</p>
      </div>

      <div v-else class="orders">
        <div v-for="order in orders" :key="order.id" class="order">
          <div class="order__header">
            <h3>{{ order.description }}</h3>
            <p class="order__date">{{ formatDate(order.orderDate) }}</p>
          </div>
          <div class="order__receipt">
            <h4>Receipt</h4>
            <pre>{{ JSON.stringify(parseReceipt(order.receiptJson), null, 2) }}</pre>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.history {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
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

.loading, .empty {
  text-align: center;
  padding: 40px;
  color: var(--night-muted);
}

.orders {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 16px;
  padding: 16px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.06);
}

.order__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.order__header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.order__date {
  margin: 0;
  color: var(--night-muted);
  font-size: 14px;
}

.order__receipt h4 {
  margin: 0 0 10px 0;
  font-size: 16px;
  font-weight: 700;
}

.order__receipt pre {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 10px;
  padding: 10px;
  overflow-x: auto;
  font-size: 12px;
  color: var(--night-text);
}
</style>
