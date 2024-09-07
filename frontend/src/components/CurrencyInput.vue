<template>
  <div class="converter">
    <input
      type="number"
      v-model.number="localAmount"
      min="0"
      placeholder="Enter amount"
      class="amount-input"
      @input="emitAmount"
    />
    <select
      v-model="localSourceCurrency"
      class="currency-select"
      @change="emitSourceCurrency"
    >
      <option v-for="currency in currencies" :key="currency" :value="currency">
        {{ currency }}
      </option>
    </select>

    <button class="swap-button" @click="swapCurrencies">⇄</button>

    <select
      v-model="localTargetCurrency"
      class="currency-select"
      @change="emitTargetCurrency"
    >
      <option v-for="currency in currencies" :key="currency" :value="currency">
        {{ currency }}
      </option>
    </select>
  </div>
</template>

<script>
export default {
  props: {
    amount: {
      type: Number,
      default: 0,
    },
    sourceCurrency: {
      type: String,
      default: "USD",
    },
    targetCurrency: {
      type: String,
      default: "EUR",
    },
  },
  data() {
    return {
      localAmount: this.amount,
      localSourceCurrency: this.sourceCurrency,
      localTargetCurrency: this.targetCurrency,
      currencies: [
        "EUR",
        "GBP",
        "USD",
        "JPY",
        "AUD",
        "CAD",
        "CHF",
        "CNY",
        "SEK",
        "NZD",
        "MXN",
        "SGD",
        "HKD",
        "NOK",
        "KRW",
        "TRY",
        "RUB",
        "INR",
        "BRL",
        "ZAR",
      ],
    };
  },
  watch: {
    amount(newAmount) {
      this.localAmount = newAmount;
    },
    sourceCurrency(newSource) {
      this.localSourceCurrency = newSource;
    },
    targetCurrency(newTarget) {
      this.localTargetCurrency = newTarget;
    },
  },
  methods: {
    emitAmount() {
      if (isNaN(this.localAmount) || this.localAmount < 0) {
        console.warn("Invalid amount entered.");
        return;
      }
      this.$emit("update-amount", this.localAmount);
    },
    emitSourceCurrency() {
      this.$emit("update-source", this.localSourceCurrency);
    },
    emitTargetCurrency() {
      this.$emit("update-target", this.localTargetCurrency);
    },
    swapCurrencies() {
      [this.localSourceCurrency, this.localTargetCurrency] = [
        this.localTargetCurrency,
        this.localSourceCurrency,
      ];
      this.emitSourceCurrency();
      this.emitTargetCurrency();
    },
  },
};
</script>

<style scoped>
.converter {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  max-width: 565px;
}

.amount-input,
.currency-select {
  padding: 15px;
  margin: 0 10px;
  font-size: 1.2em;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.swap-button {
  background-color: #fff;
  color: #0e0eff;
  border: solid;
  padding: 10px;
  margin: 0 10px;
  cursor: pointer;
  border-radius: 50%;
  font-size: 20px;
  border-color: #808080;
  max-width: 3rem;
  margin: auto;
}
</style>
