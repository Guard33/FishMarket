import { computed, ref, watch } from 'vue'
import { useAuth } from './useAuth'

const cartItems = ref([])
const isHydrated = ref(false)
const saveTimeout = ref(null)
let watchersInitialized = false

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
  localStorage.removeItem('cart_items')
}

const hydrateFromStorage = () => {
  const stored = localStorage.getItem('cart_items')
  if (stored) {
    try {
      cartItems.value = JSON.parse(stored)
    } catch {
      cartItems.value = []
    }
  }
}

const itemCount = computed(() =>
  cartItems.value.reduce((total, item) => total + (item.quantity || 0), 0),
)

const subtotal = computed(() =>
  cartItems.value.reduce((total, item) => total + item.price * item.quantity, 0),
)

const persistCart = (authorizedRequest) => {
  if (saveTimeout.value) {
    clearTimeout(saveTimeout.value)
  }
  saveTimeout.value = setTimeout(async () => {
    try {
      await authorizedRequest({
        method: 'PUT',
        url: '/api/cart',
        data: cartItems.value,
      })
    } catch {
      localStorage.setItem('cart_items', JSON.stringify(cartItems.value))
    }
  }, 350)
}

export function useCart() {
  const { authToken, isAuthenticated, authorizedRequest } = useAuth()

  if (!isHydrated.value) {
    isHydrated.value = true
    if (isAuthenticated.value) {
      authorizedRequest({ method: 'GET', url: '/api/cart' })
        .then(({ data }) => {
          try {
            cartItems.value = JSON.parse(data.cartJson || '[]')
          } catch {
            cartItems.value = []
          }
        })
        .catch(hydrateFromStorage)
    } else {
      hydrateFromStorage()
    }
  }

  if (!watchersInitialized) {
    watchersInitialized = true
    watch(
      () => authToken.value,
      (token) => {
        if (token) {
          authorizedRequest({ method: 'GET', url: '/api/cart' })
            .then(({ data }) => {
              try {
                cartItems.value = JSON.parse(data.cartJson || '[]')
              } catch {
                cartItems.value = []
              }
            })
            .catch(() => {})
        } else {
          clearCart()
        }
      },
    )

    watch(
      cartItems,
      () => {
        if (isAuthenticated.value) {
          persistCart(authorizedRequest)
        } else {
          localStorage.setItem('cart_items', JSON.stringify(cartItems.value))
        }
      },
      { deep: true },
    )
  }

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
