<script setup>
import { computed, onMounted, ref } from 'vue'
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { useAuth } from './composables/useAuth'
import logoUrl from './assets/fishmarket-logo.png'

const route = useRoute()
const { authUser, initGoogleButton, isAuthenticated, signOut } = useAuth()
const authError = ref('')

const navLinks = [
  { label: 'Shop', to: '/' },
  { label: 'Gear', to: '/gear' },
  { label: 'Contact', to: '/contact' },
  { label: 'Cart', to: '/cart' },
  { label: 'Checkout', to: '/checkout' },
]

const currentLabel = computed(() => {
  const match = navLinks.find((link) => link.to === route.path)
  return match ? match.label : 'Shop'
})

onMounted(() => {
  initGoogleButton('google-signin', (message) => {
    authError.value = message
  })
})
</script>

<template>
  <div class="app-shell">
    <header class="hero">
      <div class="hero__bar">
        <div class="brand">
          <div class="brand__mark">
            <img :src="logoUrl" alt="Fish Market logo" class="brand__logo" />
          </div>
          <div>
            <p class="brand__eyebrow">Tide & Scale</p>
            <p class="brand__title">Fish Market</p>
          </div>
        </div>
        <div class="nav__wrap">
          <nav class="nav">
            <RouterLink
              v-for="link in navLinks"
              :key="link.to"
              :to="link.to"
              class="nav__link"
              :class="{ 'nav__link--active': route.path === link.to }"
            >
              {{ link.label }}
            </RouterLink>
          </nav>
          <div class="auth">
            <div v-if="!isAuthenticated" id="google-signin"></div>
            <div v-else class="auth__profile">
              <img v-if="authUser?.avatarUrl" :src="authUser.avatarUrl" alt="" class="auth__avatar" />
              <div>
                <p class="auth__name">{{ authUser?.name || 'Dock Crew' }}</p>
                <button class="auth__logout" @click="signOut">Sign out</button>
              </div>
            </div>
            <p v-if="authError" class="auth__error">{{ authError }}</p>
          </div>
        </div>
      </div>
      <div class="hero__content">
        <div>
          <p class="hero__eyebrow">Fresh catch | harbor market</p>
          <h1 class="hero__headline">Market-day seafood, dock-to-door</h1>
          <p class="hero__copy">
            Browse seasonal fish, build a cart, and gear up for the next tide. Filter by price,
            weight, or length to match your recipe.
          </p>
          <div class="hero__actions">
            <RouterLink to="/" class="btn btn--primary">Shop todays catch</RouterLink>
            <RouterLink to="/gear" class="btn btn--ghost">Shop fishing gear</RouterLink>
          </div>
        </div>
        <div class="hero__card">
          <p class="hero__tag">Now viewing</p>
          <p class="hero__card-title">{{ currentLabel }}</p>
          <p class="hero__card-copy">
            Slide between shop, gear, and checkout without losing your picks. Cold-chain ready.
          </p>
        </div>
      </div>
    </header>

    <main class="page">
      <RouterView />
    </main>

    <footer class="footer">
      <p>Cold packed in under 24 hours. Coastal dispatch from Monterey.</p>
      <p class="footer__meta">Support: dock@fishmarket.co · (831) 555-0172</p>
    </footer>
  </div>
</template>

<style scoped>
:global(:root) {
  --night-bg: #ffffff;
  --night-surface: #ffffff;
  --night-border: #e7e2d9;
  --night-text: #1f1b16;
  --night-muted: #6f665b;
  --night-accent: #0f8f8a;
  --night-accent-strong: #0aa09a;
  --night-magenta: #e8d9c4;
}

:global(body) {
  margin: 0;
  font-family: 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
  background: var(--night-bg);
  color: var(--night-text);
}

.app-shell {
  min-height: 100vh;
  background:
    radial-gradient(120% 80% at 20% 0%, rgba(15, 143, 138, 0.08) 0%, transparent 60%),
    radial-gradient(120% 80% at 90% 10%, rgba(232, 217, 196, 0.18) 0%, transparent 60%),
    linear-gradient(140deg, #ffffff 0%, #faf7f2 60%, #ffffff 100%);
  display: flex;
  flex-direction: column;
}

.hero {
  padding: 32px 24px 16px;
  border-bottom: 1px solid var(--night-border);
}

.hero__bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand__mark {
  width: 58px;
  height: 58px;
  border-radius: 14px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  display: grid;
  place-items: center;
  box-shadow: 0 10px 24px rgba(31, 27, 23, 0.12);
  overflow: hidden;
}

.brand__logo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  transform: scale(1.25);
}

.brand__eyebrow {
  color: var(--night-muted);
  text-transform: uppercase;
  font-size: 12px;
  margin: 0;
  letter-spacing: 0.08em;
}

.brand__title {
  margin: 2px 0 0;
  font-size: 18px;
  font-weight: 700;
}

.nav__wrap {
  display: flex;
  align-items: center;
  gap: 18px;
}

.nav {
  display: flex;
  gap: 12px;
}

.nav__link {
  color: #2f2822;
  text-decoration: none;
  padding: 8px 14px;
  border-radius: 12px;
  border: 1px solid transparent;
  transition: all 0.2s ease;
  font-weight: 600;
}

.nav__link:hover {
  border-color: rgba(47, 40, 34, 0.12);
  background: #ffffff;
}

.nav__link--active {
  background: #e9f6f5;
  color: #1f1b17;
  border-color: rgba(15, 143, 138, 0.2);
  box-shadow: 0 10px 25px rgba(15, 143, 138, 0.2);
}

.hero__content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  align-items: center;
}

.hero__eyebrow {
  color: var(--night-accent);
  text-transform: uppercase;
  letter-spacing: 0.12em;
  font-size: 12px;
  margin-bottom: 8px;
}

.hero__headline {
  font-size: clamp(26px, 5vw, 40px);
  margin: 0 0 12px;
  color: var(--night-text);
}

.hero__copy {
  margin: 0 0 16px;
  color: var(--night-muted);
  max-width: 640px;
  line-height: 1.6;
}

.hero__actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.btn {
  padding: 12px 16px;
  border-radius: 12px;
  border: 1px solid transparent;
  font-weight: 700;
  cursor: pointer;
  text-decoration: none;
  transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
}

.btn--primary {
  background: linear-gradient(120deg, #0f8f8a, #0aa09a);
  color: #ffffff;
  box-shadow: 0 12px 30px rgba(15, 143, 138, 0.25);
}

.btn--ghost {
  color: #2f2822;
  border-color: rgba(47, 40, 34, 0.16);
  background: rgba(255, 255, 255, 0.8);
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 30px rgba(15, 143, 138, 0.2);
}

.hero__card {
  background: var(--night-surface);
  border: 1px solid var(--night-border);
  padding: 18px;
  border-radius: 16px;
  backdrop-filter: blur(8px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.08);
}

.hero__tag {
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--night-accent);
  margin: 0 0 6px;
}

.hero__card-title {
  margin: 0 0 6px;
  font-size: 18px;
  font-weight: 700;
}

.hero__card-copy {
  margin: 0;
  color: var(--night-muted);
  line-height: 1.5;
}

.page {
  flex: 1;
  padding: 20px 24px 32px;
}

.footer {
  padding: 16px 24px 28px;
  border-top: 1px solid var(--night-border);
  color: var(--night-muted);
}

.footer__meta {
  margin: 4px 0 0;
  color: #7a8aa8;
}

.auth {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 6px;
  min-width: 180px;
}

.auth__profile {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #ffffff;
  border: 1px solid var(--night-border);
  padding: 6px 10px;
  border-radius: 999px;
}

.auth__avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid rgba(47, 40, 34, 0.2);
}

.auth__name {
  margin: 0;
  font-weight: 700;
  font-size: 12px;
}

.auth__logout {
  background: transparent;
  border: none;
  color: var(--night-accent);
  font-size: 12px;
  cursor: pointer;
  padding: 0;
}

.auth__error {
  margin: 0;
  color: #ff9ccf;
  font-size: 12px;
}

@media (max-width: 700px) {
  .hero__bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .nav {
    flex-wrap: wrap;
  }

  .nav__wrap {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
