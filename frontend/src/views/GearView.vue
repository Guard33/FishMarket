<script setup>
import { onMounted, ref } from 'vue'
import { apiBaseUrl } from '../composables/useAuth'
import { useCart } from '../composables/useCart'

const gearInventory = ref([])
const loading = ref(true)
const error = ref('')
const { addToCart } = useCart()

onMounted(async () => {
  try {
    const response = await fetch(`${apiBaseUrl}/api/gear`)
    if (!response.ok) {
      throw new Error('Unable to load fishing gear.')
    }
    gearInventory.value = await response.json()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unable to load fishing gear.'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <section class="gear">
    <div class="gear__header">
      <div>
        <p class="pill">Fishing gear</p>
        <h2>Harbor-ready essentials</h2>
        <p class="lede">
          Rods, reels, and tackle curated for everyday coastal runs.
        </p>
      </div>
      <div class="gear__glow">Dockside favorites</div>
    </div>

    <div v-if="loading" class="status">Loading gear...</div>
    <div v-else-if="error" class="status status--error">{{ error }}</div>
    <div v-else class="gear__grid">
      <article v-for="item in gearInventory" :key="item.id" class="gear-card">
        <div class="gear-card__price">${{ item.price }}</div>
        <h3 class="gear-card__title">{{ item.name }}</h3>
        <p class="gear-card__notes">{{ item.notes }}</p>
        <button class="btn btn--ghost" @click="addToCart(item)">Add to cart</button>
      </article>
    </div>
  </section>
</template>

<style scoped>
.gear {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 18px;
  padding: 20px;
  box-shadow: 0 20px 40px rgba(31, 27, 23, 0.12);
}

.gear__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.gear__glow {
  padding: 10px 16px;
  border-radius: 999px;
  background: linear-gradient(120deg, rgba(15, 143, 138, 0.2), rgba(201, 109, 96, 0.12));
  color: #2f2822;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  font-size: 12px;
}

.pill {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(15, 143, 138, 0.12);
  color: var(--night-accent);
  font-weight: 700;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.lede {
  color: var(--night-muted);
  max-width: 520px;
}

.gear__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 14px;
}

.status {
  padding: 18px;
  border-radius: 14px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  color: var(--night-muted);
  font-weight: 600;
}

.status--error {
  color: #ffb4b4;
  border-color: rgba(255, 180, 180, 0.4);
}

.gear-card {
  padding: 16px;
  border-radius: 16px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.5);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.gear-card__price {
  font-weight: 800;
  color: var(--night-accent);
}

.gear-card__title {
  margin: 0;
}

.gear-card__notes {
  margin: 0;
  color: var(--night-muted);
}
</style>
