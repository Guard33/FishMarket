import { computed, ref } from 'vue'

export const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID || ''

const authToken = ref(localStorage.getItem('auth_token') || '')
const authUser = ref(
  JSON.parse(localStorage.getItem('auth_user') || 'null'),
)

const isAuthenticated = computed(() => Boolean(authToken.value))

const setAuth = (token, profile) => {
  authToken.value = token
  authUser.value = profile
  localStorage.setItem('auth_token', token)
  localStorage.setItem('auth_user', JSON.stringify(profile))
}

const clearAuth = () => {
  authToken.value = ''
  authUser.value = null
  localStorage.removeItem('auth_token')
  localStorage.removeItem('auth_user')
  if (window.google?.accounts?.id?.disableAutoSelect) {
    window.google.accounts.id.disableAutoSelect()
  }
}

const authorizedFetch = (path, options = {}) => {
  const headers = new Headers(options.headers || {})
  headers.set('Content-Type', headers.get('Content-Type') || 'application/json')
  if (authToken.value) {
    headers.set('Authorization', `Bearer ${authToken.value}`)
  }
  return fetch(`${apiBaseUrl}${path}`, { ...options, headers })
}

const signInWithGoogle = async (credential) => {
  const response = await fetch(`${apiBaseUrl}/api/auth/google`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ token: credential }),
  })
  if (!response.ok) {
    throw new Error('Unable to sign in with Google.')
  }
  const data = await response.json()
  setAuth(data.token, { name: data.name, email: data.email, avatarUrl: data.avatarUrl })
}

const loadGoogleScript = () =>
  new Promise((resolve, reject) => {
    if (document.getElementById('google-identity')) {
      resolve()
      return
    }
    const script = document.createElement('script')
    script.src = 'https://accounts.google.com/gsi/client'
    script.async = true
    script.defer = true
    script.id = 'google-identity'
    script.onload = () => resolve()
    script.onerror = () => reject(new Error('Failed to load Google identity script.'))
    document.head.appendChild(script)
  })

const initGoogleButton = async (elementId, onError) => {
  if (!googleClientId) {
    onError?.('Missing VITE_GOOGLE_CLIENT_ID.')
    return
  }
  try {
    await loadGoogleScript()
    if (!window.google?.accounts?.id) {
      onError?.('Google Identity Services unavailable.')
      return
    }
    const target = document.getElementById(elementId)
    if (!target) {
      return
    }
    target.innerHTML = ''
    window.google.accounts.id.initialize({
      client_id: googleClientId,
      callback: async (response) => {
        try {
          await signInWithGoogle(response.credential)
        } catch (err) {
          onError?.(err instanceof Error ? err.message : 'Unable to sign in with Google.')
        }
      },
    })
    window.google.accounts.id.renderButton(target, {
      theme: 'filled_black',
      size: 'large',
      shape: 'pill',
      text: 'signin_with',
    })
  } catch (err) {
    onError?.(err instanceof Error ? err.message : 'Unable to initialize Google login.')
  }
}

export function useAuth() {
  return {
    authToken,
    authUser,
    isAuthenticated,
    initGoogleButton,
    signOut: clearAuth,
    authorizedFetch,
  }
}
