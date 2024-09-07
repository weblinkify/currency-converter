<template>
  <div class="container">
    <h1 class="heading">CURRENCY CONVERTER</h1>
    <CurrencyInput
      :amount="amount"
      :sourceCurrency="sourceCurrency"
      :targetCurrency="targetCurrency"
      @update-amount="updateAmount"
      @update-source="updateSourceCurrency"
      @update-target="updateTargetCurrency"
      @swap-currencies="swapCurrencies"
    />
    <div class="convert-container">
      <button
        class="convert-button"
        @click="convertCurrency"
        :disabled="isLoading"
        aria-live="polite"
      >
        Convert
        <span v-if="isLoading" class="spinner" aria-hidden="true"></span>
      </button>
    </div>
    <div class="message-container">
      <p :class="errorClass">{{ errorMessage }}</p>
      <p :class="resultClass">
        {{ amount }} {{ sourceCurrency }} = {{ conversionResult }}
        {{ targetCurrency }}
      </p>
    </div>
  </div>
</template>

<script>
import CurrencyInput from "./CurrencyInput.vue";
import { convertCurrency } from "../service/CurrencyService.js";

export default {
  components: {
    CurrencyInput,
  },
  data() {
    return {
      amount: 0,
      sourceCurrency: "EUR",
      targetCurrency: "GBP",
      conversionResult: null,
      isError: false,
      isLoading: false,
      errorMessage: "",
    };
  },
  watch: {
    amount() {
      this.resetConversion();
    },
    sourceCurrency() {
      this.resetConversion();
    },
    targetCurrency() {
      this.resetConversion();
    },
  },
  methods: {
    async convertCurrency() {
      this.isLoading = true;
      this.isError = false;
      this.errorMessage = "";
      this.conversionResult = null;

      try {
        const result = await convertCurrency(
          this.sourceCurrency,
          this.targetCurrency,
          this.amount
        );
        this.conversionResult = result;
      } catch (error) {
        this.errorMessage = `${error.message}`;
        this.isError = true;
      } finally {
        this.isLoading = false;
      }
    },
    updateAmount(newAmount) {
      this.amount = newAmount;
    },
    updateSourceCurrency(newSource) {
      this.sourceCurrency = newSource;
    },
    updateTargetCurrency(newTarget) {
      this.targetCurrency = newTarget;
    },
    swapCurrencies() {
      [this.sourceCurrency, this.targetCurrency] = [
        this.targetCurrency,
        this.sourceCurrency,
      ];
    },
    resetConversion() {
      this.conversionResult = null;
      this.isError = false;
      this.isLoading = false;
      this.errorMessage = "";
    },
  },
  computed: {
    resultClass() {
      return this.conversionResult !== null ? "result show" : "result";
    },
    errorClass() {
      return this.isError ? "error show" : "error";
    },
  },
};
</script>

<style scoped>
/* General styles */
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: calc(100vh - 20px);
  background-color: #f8f9fa;
  margin: 10px;
  padding: 10px;
}

.heading {
  font-size: 2em;
  font-weight: bold;
  margin-bottom: 20px;
}

.convert-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.convert-button {
  padding: 15px 20px;
  margin-top: 15px;
  background-color: #007bff;
  color: white;
  border: none;
  font-size: 1.2em;
  border-radius: 4px;
  cursor: pointer;
  position: relative;
  width: 120px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.convert-button:disabled {
  background-color: #0056b3;
  cursor: not-allowed;
}

.convert-button:hover:not(:disabled) {
  background-color: #0056b3;
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left: 4px solid white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  animation: spin 1s linear infinite;
  position: absolute;
  right: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.message-container {
  margin-top: 20px;
  width: 100%;
  text-align: center;
  min-height: 2em;
}

.result,
.error {
  font-size: 1.4em;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.result.show,
.error.show {
  opacity: 1;
}

.result {
  color: #28a745;
}

.error {
  color: #dc3545;
}

/* Currency Input Styles */
.converter {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  max-width: 565px;
  width: 100%;
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
}

/* Responsive Styles */
@media (max-width: 768px) {
  .container {
    padding: 5px;
  }

  .heading {
    font-size: 1.5em;
    margin-bottom: 10px;
  }

  .convert-button {
    font-size: 1em;
    width: auto;
    padding: 10px 15px;
  }

  .spinner {
    width: 15px;
    height: 15px;
  }

  .message-container {
    margin-top: 15px;
  }

  .result,
  .error {
    font-size: 1.2em;
  }

  .converter {
    flex-direction: column;
    align-items: stretch;
    padding: 15px;
  }

  .amount-input,
  .currency-select {
    margin: 10px 0;
    font-size: 1em;
  }

  .swap-button {
    margin: 10px 0;
    font-size: 18px;
    margin-top: 11px;
    max-width: 3rem;
    margin: auto;
  }
}

@media (max-width: 480px) {
  .heading {
    font-size: 1.2em;
  }

  .convert-button {
    font-size: 0.9em;
  }

  .spinner {
    width: 10px;
    height: 10px;
  }

  .result,
  .error {
    font-size: 1em;
  }
}
</style>
