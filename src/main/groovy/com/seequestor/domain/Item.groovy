package com.seequestor.domain

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.util.function.IntFunction

@CompileStatic
@ToString(includes = 'sku')
@TupleConstructor
@EqualsAndHashCode(includes = 'sku')
class Item {
    String sku
    IntFunction<Integer> rule
}
