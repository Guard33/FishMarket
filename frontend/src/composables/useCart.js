import { computed, ref } from 'vue'

const cartItems = ref([])

const addToCart = (product) => {
  const existing = cartItems.value.find((item) => item.id === product.id)
  if (existing) {
    existing.quantity += 1
  } else {
    cartItems.value.push({ ...product, quantity: 1 })
  }
}

const updateQuantity = (id, quantity) => {
  const target = cartItems.value.find((item) => item.id === id)
  if (target) {
    target.quantity = Math.max(1, quantity)
  }
}

const removeFromCart = (id) => {
  cartItems.value = cartItems.value.filter((item) => item.id !== id)
}

const clearCart = () => {
  cartItems.value = []
}

const itemCount = computed(() =>
  cartItems.value.reduce((total, item) => total + (item.quantity || 0), 0),
)

const subtotal = computed(() =>
  cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0),
)

export function useCart() {
  return {
    cartItems,
    addToCart,
    updateQuantity,
    removeFromCart,
    clearCart,
    itemCount,
    subtotal,
  }
}
