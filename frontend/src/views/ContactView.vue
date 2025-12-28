<script setup>
import { ref } from 'vue'

const form = ref({
  name: '',
  email: '',
  message: '',
  topic: 'order',
})

const submitted = ref(false)

const submit = () => {
  submitted.value = true
}
</script>

<template>
  <section class="contact">
    <div>
      <p class="pill">Contact</p>
      <h2>We are on deck for your questions</h2>
      <p class="lede">
        Reach the dock crew about sourcing, prep tips, or wholesale orders. We reply within one
        business day.
      </p>

      <div class="contact__details">
        <div>
          <p class="label">Call</p>
          <p class="value">(831) 555-0172</p>
        </div>
        <div>
          <p class="label">Email</p>
          <p class="value">dock@fishmarket.co</p>
        </div>
        <div>
          <p class="label">Hours</p>
          <p class="value">Mon-Sat · 7a-6p PST</p>
        </div>
      </div>
    </div>

    <form class="panel" @submit.prevent="submit">
      <div class="panel__header">
        <p class="panel__title">Drop us a note</p>
        <p class="panel__copy">Share a few details and we will route you to the right specialist.</p>
      </div>

      <label class="field">
        <span>Name</span>
        <input v-model="form.name" type="text" placeholder="Jamie Fisher" required />
      </label>

      <label class="field">
        <span>Email</span>
        <input v-model="form.email" type="email" placeholder="you@kitchen.com" required />
      </label>

      <label class="field">
        <span>Topic</span>
        <select v-model="form.topic">
          <option value="order">Order status</option>
          <option value="sourcing">Sourcing question</option>
          <option value="wholesale">Wholesale</option>
          <option value="other">Other</option>
        </select>
      </label>

      <label class="field">
        <span>Message</span>
        <textarea
          v-model="form.message"
          rows="4"
          placeholder="What can we help with?"
          required
        ></textarea>
      </label>

      <button class="btn btn--primary" type="submit">Send message</button>

      <p v-if="submitted" class="success">
        Sent! We will respond shortly with details.
      </p>
    </form>
  </section>
</template>

<style scoped>
.contact {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
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
  color: var(--night-muted);
  max-width: 540px;
}

.contact__details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
  margin-top: 14px;
}

.label {
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--night-accent);
  font-size: 12px;
}

.value {
  margin: 4px 0 0;
  font-weight: 700;
}

.panel {
  background: #ffffff;
  border: 1px solid var(--night-border);
  border-radius: 16px;
  padding: 16px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.06);
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
  color: var(--night-muted);
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-top: 10px;
  color: #d7e5ff;
}

.field input,
.field select,
.field textarea {
  padding: 10px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: rgba(255, 255, 255, 0.04);
  color: var(--night-text);
}

.success {
  margin: 10px 0 0;
  color: #63e6be;
  font-weight: 700;
}
</style>
