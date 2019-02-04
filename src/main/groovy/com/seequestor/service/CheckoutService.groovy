package com.seequestor.service

import groovy.transform.CompileStatic

@CompileStatic
interface CheckoutService {
    CheckoutService scan(final String sku)

    CheckoutService cancel(final String sku)

    Long total()
}
