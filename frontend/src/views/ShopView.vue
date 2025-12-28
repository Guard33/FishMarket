<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { useCart } from '../composables/useCart'

const { addToCart, itemCount, subtotal } = useCart()

const fishInventory = ref([])
const loading = ref(true)
const error = ref('')

const maxPriceInData = computed(() =>
  fishInventory.value.length ? Math.max(...fishInventory.value.map((item) => item.price)) : 0,
)
const minWeightInData = computed(() =>
  fishInventory.value.length ? Math.min(...fishInventory.value.map((item) => item.weight)) : 0,
)
const maxWeightInData = computed(() =>
  fishInventory.value.length ? Math.max(...fishInventory.value.map((item) => item.weight)) : 0,
)
const minLengthInData = computed(() =>
  fishInventory.value.length ? Math.min(...fishInventory.value.map((item) => item.length)) : 0,
)
const maxLengthInData = computed(() =>
  fishInventory.value.length ? Math.max(...fishInventory.value.map((item) => item.length)) : 0,
)

const priceCeiling = ref(0)
const weightMin = ref(0)
const weightMax = ref(0)
const lengthMin = ref(0)
const lengthMax = ref(0)
const search = ref('')

const setFilterDefaults = () => {
  if (!fishInventory.value.length) {
    priceCeiling.value = 0
    weightMin.value = 0
    weightMax.value = 0
    lengthMin.value = 0
    lengthMax.value = 0
    return
  }

  priceCeiling.value = Math.ceil(maxPriceInData.value)
  weightMin.value = minWeightInData.value
  weightMax.value = maxWeightInData.value
  lengthMin.value = minLengthInData.value
  lengthMax.value = maxLengthInData.value
}

watch(fishInventory, setFilterDefaults, { immediate: true })

const filteredFish = computed(() =>
  fishInventory.value.filter((fish) => {
    const matchesPrice = fish.price <= priceCeiling.value
    const matchesWeight = fish.weight >= weightMin.value && fish.weight <= weightMax.value
    const matchesLength = fish.length >= lengthMin.value && fish.length <= lengthMax.value
    const matchesSearch =
      !search.value ||
      fish.name.toLowerCase().includes(search.value.toLowerCase()) ||
      fish.origin.toLowerCase().includes(search.value.toLowerCase())

    return matchesPrice && matchesWeight && matchesLength && matchesSearch
  }),
)

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

onMounted(async () => {
  try {
    const response = await fetch(`${apiBaseUrl}/api/fish`)
    if (!response.ok) {
      throw new Error('Unable to load fish inventory.')
    }
    const data = await response.json()
    fishInventory.value = data.map((fish) => ({
      ...fish,
      price: Number(fish.price),
      weight: Number(fish.weight),
      length: Number(fish.length),
    }))
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Unable to load fish inventory.'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <section class="grid">
    <div class="panel filters">
      <div class="panel__header">
        <p class="pill">Filters</p>
        <p class="panel__title">Dial in the catch you want</p>
        <p class="panel__copy">Slide the ranges to match your recipe and portion sizes.</p>
      </div>

      <div class="filter">
        <div class="filter__label">
          <p class="filter__title">Price</p>
          <p class="filter__value">Up to ${{ priceCeiling }}</p>
        </div>
        <input
          v-model.number="priceCeiling"
          type="range"
          :min="10"
          :max="Math.ceil(maxPriceInData)"
          step="1"
        />
        <div class="filter__scale">
          <span>$10</span>
          <span>${{ Math.ceil(maxPriceInData) }}</span>
        </div>
      </div>

      <div class="filter two-col">
        <div>
          <p class="filter__title">Min weight (lb)</p>
          <input v-model.number="weightMin" type="number" :min="0.5" :max="weightMax" step="0.1" />
        </div>
        <div>
          <p class="filter__title">Max weight (lb)</p>
          <input
            v-model.number="weightMax"
            type="number"
            :min="weightMin"
            :max="Math.ceil(maxWeightInData)"
            step="0.1"
          />
        </div>
      </div>

      <div class="filter two-col">
        <div>
          <p class="filter__title">Min length (in)</p>
          <input v-model.number="lengthMin" type="number" :min="6" :max="lengthMax" step="1" />
        </div>
        <div>
          <p class="filter__title">Max length (in)</p>
          <input
            v-model.number="lengthMax"
            type="number"
            :min="lengthMin"
            :max="Math.ceil(maxLengthInData)"
            step="1"
          />
        </div>
      </div>

      <div class="filter">
        <div class="filter__label">
          <p class="filter__title">Search</p>
          <p class="filter__value">Name or origin</p>
        </div>
        <input v-model="search" type="text" placeholder="Search by species or location" />
      </div>

      <div class="panel__footer">
        <p>
          Showing
          <strong>{{ filteredFish.length }}</strong>
          of {{ fishInventory.length }} items
        </p>
        <RouterLink class="link" to="/cart">
          Cart: {{ itemCount }} item<span v-if="itemCount !== 1">s</span> - ${{ subtotal.toFixed(2) }}
        </RouterLink>
      </div>
    </div>

    <div class="products">
      <div class="products__header">
        <div>
          <p class="pill pill--faint">Seafood shop</p>
          <h2 class="products__title">Fresh coastal cuts</h2>
          <p class="products__copy">Packed same day with gel-cooling and traceable sourcing.</p>
        </div>
        <RouterLink to="/checkout" class="btn btn--ghost">Skip to checkout</RouterLink>
      </div>

      <div v-if="loading" class="status">Loading fish inventory...</div>
      <div v-else-if="error" class="status status--error">{{ error }}</div>
      <div v-else-if="!filteredFish.length" class="status">
        No fish match these filters yet.
      </div>
      <div v-else class="product-grid">
        <article v-for="fish in filteredFish" :key="fish.id" class="card">
          <div class="card__badge">${{ fish.price }} / lb</div>
          <div class="card__body">
            <p class="card__eyebrow">{{ fish.origin }}</p>
            <h3 class="card__title">{{ fish.name }}</h3>
            <p class="card__notes">{{ fish.notes }}</p>
            <div class="card__meta">
              <span>{{ fish.weight }} lb avg</span>
              <span>{{ fish.length }} in length</span>
            </div>
          </div>
          <div class="card__actions">
            <button class="btn btn--primary" @click="addToCart(fish)">Add to cart</button>
            <RouterLink class="link" to="/cart">View cart</RouterLink>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 16px;
}

.panel {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 16px;
  padding: 16px;
  height: fit-content;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.06);
}

.panel__header {
  margin-bottom: 12px;
}

.panel__title {
  margin: 4px 0;
  font-weight: 700;
  font-size: 18px;
}

.panel__copy {
  margin: 0;
  color: var(--night-muted);
}

.panel__footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  color: var(--night-muted);
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

.pill--faint {
  background: rgba(255, 255, 255, 0.06);
  color: #a7c3e7;
}

.filter {
  margin-top: 12px;
  padding: 10px;
  border-radius: 12px;
  background: #ffffff;
  border: 1px solid var(--night-border);
}

.filter__label {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.filter__title {
  margin: 0 0 4px;
  font-weight: 700;
}

.filter__value {
  margin: 0;
  color: var(--night-muted);
}

.filter input[type='range'] {
  width: 100%;
  accent-color: var(--night-accent-strong);
}

.filter input[type='number'],
.filter input[type='text'] {
  width: 100%;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.04);
  color: var(--night-text);
  box-sizing: border-box;
}

.filter__scale {
  display: flex;
  justify-content: space-between;
  color: var(--night-muted);
  font-size: 12px;
}

.two-col {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.two-col > div {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
}

.products {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 18px;
  padding: 18px;
}

.products__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.products__title {
  margin: 6px 0;
  font-size: 22px;
}

.products__copy {
  margin: 0;
  color: var(--night-muted);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
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

.card {
  position: relative;
  border-radius: 16px;
  padding: 14px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.6), 0 18px 32px rgba(31, 27, 23, 0.08);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.card__badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 6px 10px;
  border-radius: 10px;
  background: #ffffff;
  color: var(--night-accent);
  font-weight: 800;
  font-size: 12px;
}

.card__eyebrow {
  margin: 0;
  text-transform: uppercase;
  font-size: 12px;
  color: var(--night-accent);
  letter-spacing: 0.08em;
}

.card__title {
  margin: 4px 0;
}

.card__notes {
  margin: 0;
  color: var(--night-muted);
}

.card__meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  color: #1a2638;
  font-weight: 700;
}

.card__meta span {
  padding: 6px 10px;
  border-radius: 10px;
  background: rgba(15, 143, 138, 0.08);
  color: #2f2822;
  border: 1px solid var(--night-border);
}

.card__actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.link {
  color: #8bd5ff;
  text-decoration: none;
  font-weight: 700;
}

@media (max-width: 900px) {
  .grid {
    grid-template-columns: 1fr;
  }

  .panel {
    position: sticky;
    top: 8px;
    z-index: 1;
  }
}

@media (max-width: 600px) {
  .card__actions {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .panel__footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>
