<script setup>
import { computed } from 'vue'
import { RouterLink, RouterView, useRoute } from 'vue-router'

const route = useRoute()

const navLinks = [
  { label: 'Shop', to: '/' },
  { label: 'Contact', to: '/contact' },
  { label: 'Cart', to: '/cart' },
  { label: 'Checkout', to: '/checkout' },
]

const currentLabel = computed(() => {
  const match = navLinks.find((link) => link.to === route.path)
  return match ? match.label : 'Shop'
})
</script>

<template>
  <div class="app-shell">
    <header class="hero">
      <div class="hero__bar">
        <div class="brand">
          <div class="brand__mark">FM</div>
          <div>
            <p class="brand__eyebrow">Tide & Scale</p>
            <p class="brand__title">Fish Market</p>
          </div>
        </div>
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
      </div>
      <div class="hero__content">
        <div>
          <p class="hero__eyebrow">Fresh catch, coastal quality</p>
          <h1 class="hero__headline">Sustainable seafood for discerning cooks</h1>
          <p class="hero__copy">
            Browse curated seasonal fish, build a cart, and check out securely. Filter by price,
            weight, or length to match your next recipe.
          </p>
          <div class="hero__actions">
            <RouterLink to="/" class="btn btn--primary">Shop todays catch</RouterLink>
            <RouterLink to="/contact" class="btn btn--ghost">Talk with us</RouterLink>
          </div>
        </div>
        <div class="hero__card">
          <p class="hero__tag">Now viewing</p>
          <p class="hero__card-title">{{ currentLabel }}</p>
          <p class="hero__card-copy">
            Slide between shop, cart, and checkout without losing your picks. Cold-chain ready.
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
:global(body) {
  margin: 0;
  font-family: 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
  background: #0f1624;
  color: #e6edf7;
}

.app-shell {
  min-height: 100vh;
  background: radial-gradient(120% 80% at 10% 10%, #1f2e45 0%, #0f1624 45%, #0c101b 100%);
  display: flex;
  flex-direction: column;
}

.hero {
  padding: 32px 24px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
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
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: linear-gradient(140deg, #63e6be 0%, #3fb8ff 60%, #365bff 100%);
  color: #0c101b;
  display: grid;
  place-items: center;
  font-weight: 800;
  letter-spacing: 0.08em;
  box-shadow: 0 10px 30px rgba(68, 198, 255, 0.35);
}

.brand__eyebrow {
  color: #6acffb;
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

.nav {
  display: flex;
  gap: 12px;
}

.nav__link {
  color: #d7e5ff;
  text-decoration: none;
  padding: 8px 14px;
  border-radius: 12px;
  border: 1px solid transparent;
  transition: all 0.2s ease;
  font-weight: 600;
}

.nav__link:hover {
  border-color: rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.06);
}

.nav__link--active {
  background: linear-gradient(120deg, #63e6be, #4fc3ff);
  color: #0b1220;
  box-shadow: 0 10px 25px rgba(79, 195, 255, 0.3);
}

.hero__content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  align-items: center;
}

.hero__eyebrow {
  color: #7bd6ff;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  font-size: 12px;
  margin-bottom: 8px;
}

.hero__headline {
  font-size: clamp(26px, 5vw, 40px);
  margin: 0 0 12px;
  color: #f5fbff;
}

.hero__copy {
  margin: 0 0 16px;
  color: #bcd1ec;
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
  background: linear-gradient(120deg, #63e6be, #4fc3ff);
  color: #0c101b;
  box-shadow: 0 12px 30px rgba(79, 195, 255, 0.4);
}

.btn--ghost {
  color: #d7e5ff;
  border-color: rgba(255, 255, 255, 0.16);
  background: rgba(255, 255, 255, 0.04);
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 30px rgba(79, 195, 255, 0.35);
}

.hero__card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  padding: 18px;
  border-radius: 16px;
  backdrop-filter: blur(8px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.08);
}

.hero__tag {
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #7bd6ff;
  margin: 0 0 6px;
}

.hero__card-title {
  margin: 0 0 6px;
  font-size: 18px;
  font-weight: 700;
}

.hero__card-copy {
  margin: 0;
  color: #bcd1ec;
  line-height: 1.5;
}

.page {
  flex: 1;
  padding: 20px 24px 32px;
}

.footer {
  padding: 16px 24px 28px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  color: #8fa7c6;
}

.footer__meta {
  margin: 4px 0 0;
  color: #6d85a7;
}

@media (max-width: 700px) {
  .hero__bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .nav {
    flex-wrap: wrap;
  }
}
</style>
