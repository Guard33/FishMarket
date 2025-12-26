<script setup>
const gearInventory = [
  {
    id: 'nightline-rod',
    name: 'Nightline Carbon Rod',
    price: 189,
    notes: 'Ultra-light carbon fiber, 7 ft, fast action',
  },
  {
    id: 'tidecaster-reel',
    name: 'Tidecaster 3000 Reel',
    price: 149,
    notes: 'Sealed drag, saltwater-ready, 10+1 bearings',
  },
  {
    id: 'glowjet-lures',
    name: 'Glowjet Lure Set',
    price: 42,
    notes: 'Bioluminescent soft plastics for night bites',
  },
  {
    id: 'harborline-braid',
    name: 'Harborline Braid',
    price: 24,
    notes: '30 lb test, stealth green, 300 yd spool',
  },
  {
    id: 'shorelantern',
    name: 'Shore Lantern Headlamp',
    price: 38,
    notes: 'Red light mode, rechargeable, 350 lumens',
  },
  {
    id: 'dockbox',
    name: 'Dockside Tackle Box',
    price: 65,
    notes: 'Modular trays, waterproof seal, carry strap',
  },
]
</script>

<template>
  <section class="gear">
    <div class="gear__header">
      <div>
        <p class="pill">Fishing gear</p>
        <h2>Night market essentials</h2>
        <p class="lede">
          Rods, reels, and neon-ready tackle curated for late tide runs.
        </p>
      </div>
      <div class="gear__glow">Open till dawn</div>
    </div>

    <div class="gear__grid">
      <article v-for="item in gearInventory" :key="item.id" class="gear-card">
        <div class="gear-card__price">${{ item.price }}</div>
        <h3 class="gear-card__title">{{ item.name }}</h3>
        <p class="gear-card__notes">{{ item.notes }}</p>
        <button class="btn btn--ghost">Add to gear list</button>
      </article>
    </div>
  </section>
</template>

<style scoped>
.gear {
  background: rgba(12, 10, 24, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 18px;
  padding: 20px;
  box-shadow: 0 20px 40px rgba(6, 5, 15, 0.6);
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
  background: linear-gradient(120deg, rgba(0, 255, 209, 0.2), rgba(255, 77, 255, 0.15));
  color: #9fffe8;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  font-size: 12px;
}

.pill {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(0, 255, 209, 0.12);
  color: #7affd8;
  font-weight: 700;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.lede {
  color: #b5c4e8;
  max-width: 520px;
}

.gear__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 14px;
}

.gear-card {
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(150deg, rgba(18, 22, 40, 0.9), rgba(20, 10, 35, 0.8));
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.05);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.gear-card__price {
  font-weight: 800;
  color: #7affd8;
}

.gear-card__title {
  margin: 0;
}

.gear-card__notes {
  margin: 0;
  color: #b7c8ef;
}
</style>
