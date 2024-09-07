import { shallowMount } from '@vue/test-utils';
import CurrencyConverter from '@/components/CurrencyConverter.vue';
import CurrencyInput from '@/components/CurrencyInput.vue';
import { convertCurrency } from '@/service/CurrencyService.js';

jest.mock('@/service/CurrencyService.js', () => ({
    convertCurrency: jest.fn(),
}));

describe('CurrencyConverter.vue', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = shallowMount(CurrencyConverter, {
            stubs: {
                CurrencyInput
            }
        });
    });

    it('renders correctly', () => {
        expect(wrapper.find('.heading').text()).toBe('CURRENCY CONVERTER');
    });

    it('updates amount when CurrencyInput emits update-amount event', async () => {
        const newAmount = 100;
        await wrapper.findComponent(CurrencyInput).vm.$emit('update-amount', newAmount);
        expect(wrapper.vm.amount).toBe(newAmount);
    });

    it('updates source currency when CurrencyInput emits update-source event', async () => {
        const newSourceCurrency = 'USD';
        await wrapper.findComponent(CurrencyInput).vm.$emit('update-source', newSourceCurrency);
        expect(wrapper.vm.sourceCurrency).toBe(newSourceCurrency);
    });

    it('updates target currency when CurrencyInput emits update-target event', async () => {
        const newTargetCurrency = 'JPY';
        await wrapper.findComponent(CurrencyInput).vm.$emit('update-target', newTargetCurrency);
        expect(wrapper.vm.targetCurrency).toBe(newTargetCurrency);
    });

    it('swaps currencies when swapCurrencies is called', () => {
        wrapper.setData({ sourceCurrency: 'USD', targetCurrency: 'EUR' });
        wrapper.vm.swapCurrencies();
        expect(wrapper.vm.sourceCurrency).toBe('EUR');
        expect(wrapper.vm.targetCurrency).toBe('USD');
    });

    it('calls convertCurrency and updates conversionResult on successful conversion', async () => {
        const result = 120;
        convertCurrency.mockResolvedValue(result);
        wrapper.setData({ amount: 100, sourceCurrency: 'EUR', targetCurrency: 'GBP' });

        await wrapper.vm.convertCurrency();
        expect(convertCurrency).toHaveBeenCalledWith('EUR', 'GBP', 100);
        expect(wrapper.vm.conversionResult).toBe(result);
        expect(wrapper.vm.isLoading).toBe(false);
    });

    it('handles errors from convertCurrency correctly', async () => {
        const error = new Error('Conversion error');
        convertCurrency.mockRejectedValue(error);
        wrapper.setData({ amount: 100, sourceCurrency: 'EUR', targetCurrency: 'GBP' });

        await wrapper.vm.convertCurrency();
        expect(wrapper.vm.errorMessage).toBe('Conversion error');
        expect(wrapper.vm.isError).toBe(true);
        expect(wrapper.vm.isLoading).toBe(false);
    });

    it('resets conversion state on input change', async () => {
        wrapper.setData({ conversionResult: 120, isError: true });
        wrapper.vm.amount = 50;
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.conversionResult).toBe(null);
        expect(wrapper.vm.isError).toBe(false);
        expect(wrapper.vm.errorMessage).toBe('');
    });
});
