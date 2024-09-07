import apiClient from '../api/api';

export async function convertCurrency(fromCurrency, toCurrency, amount) {
    if (!fromCurrency || !toCurrency || isNaN(amount)) {
        throw new Error('Invalid input parameters.');
    }
    if (amount <= 0) {
        throw new Error('Please enter an amount greater than 0.');
    }

    const apiUrl = `/convert?from=${encodeURIComponent(fromCurrency)}&to=${encodeURIComponent(toCurrency)}&amount=${encodeURIComponent(amount)}`;

    try {
        const response = await apiClient.get(apiUrl);
        console.log('API Response:', response);

        const numericResult = parseFloat(response.data);
        if (isNaN(numericResult)) {
            throw new Error(`Unexpected response format. Data received: ${response.data}`);
        }

        return numericResult.toFixed(6);
    } catch (error) {
        console.error("Conversion error:", error.message || error);
        if (error.message.includes("Network Error")) {
            throw new Error('An unexpected error occurred. Please try again later or contact support if the issue persists.');
        }
        throw new Error('Failed to convert currency. Please check your input and try again.');
    }
}
