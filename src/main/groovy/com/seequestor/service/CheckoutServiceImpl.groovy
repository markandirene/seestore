package com.seequestor.service

import com.seequestor.domain.Item
import groovy.transform.CompileStatic

@CompileStatic
class CheckoutServiceImpl implements CheckoutService {

    private final List<String> basket = [].asSynchronized()

    private Set<Item> items

    CheckoutServiceImpl(final Set<Item> items) {
        if (!items) throw new IllegalArgumentException('No items supplied')
        this.items = items
    }

    @Override
    CheckoutService scan(final String sku) {
        basket << validateItem(sku)
        this
    }

    @Override
    CheckoutService cancel(final String sku) {
        if (!basket.remove(validateItem(sku))) throw new IllegalArgumentException("Cannot remove ${sku}")
        this
    }

    @Override
    Long total() {
        basket.countBy { it }.inject(0) { Integer total, String sku, Integer count ->
            items.find { it.sku == sku }.rule.apply(count) + total
        }
    }

    private String validateItem(final String sku) {
        if (!sku) throw new IllegalArgumentException('Invalid item')
        if (!items.any { it.sku == sku }) throw new IllegalArgumentException("${sku} does not exist")
        sku
    }
}
