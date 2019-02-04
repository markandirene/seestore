package com.seequestor.service

import com.seequestor.domain.Item
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title('unit tests for CheckoutServiceImpl')
@Subject(CheckoutServiceImpl)
@Unroll
class CheckoutServiceImplSpec extends Specification {

    void 'test invalid constructor'() {
        when:
        new CheckoutServiceImpl(null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'No items supplied'
    }

    void 'test invalid scan'() {
        given:
        CheckoutService checkoutService = new CheckoutServiceImpl([new Item('A')] as Set<Item>)

        when:
        checkoutService.scan(item)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == message

        where:
        item || message
        'B'  || 'B does not exist'
        null || 'Invalid item'
    }

    void 'test invalid cancel'() {
        given:
        CheckoutService checkoutService = new CheckoutServiceImpl([new Item('A')] as Set<Item>)

        when:
        checkoutService.cancel(item)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == message

        where:
        item || message
        null || 'Invalid item'
        'B'  || 'B does not exist'
    }

    void 'test invalid cancel too many items'() {
        when:
        new CheckoutServiceImpl([new Item('A')] as Set<Item>).scan('A').cancel('A').cancel('A')

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Cannot remove A'
    }

    void 'test valid scan'() {
        expect:
        new CheckoutServiceImpl([new Item('A')] as Set<Item>).scan('A').@basket == ['A']
    }

    void 'test valid cancel'() {
        given:
        CheckoutService checkoutService = new CheckoutServiceImpl([new Item('A'), new Item('B')] as Set<Item>)

        expect:
        checkoutService.scan('A').scan('B').cancel('A').@basket == ['B']
    }

    void testTotal() {
        given:
        Set<Item> items = [
                new Item('A', { it % 3 * 50 + it.intdiv(3) * 130 }),
                new Item('B', { it % 2 * 30 + it.intdiv(2) * 45 }),
                new Item('C', { it * 20 }),
                new Item('D', { it * 15 })
        ] as Set<Item>
        CheckoutService checkoutService = new CheckoutServiceImpl(items)

        expect:
        checkoutService.total() == 0
        checkoutService.scan('C').scan('D').total() == 35
        checkoutService.cancel('D').scan('C').total() == 40
        checkoutService.scan('A').scan('A').scan('A').total() == 170
        checkoutService.scan('A').scan('B').total() == 250
        checkoutService.scan('A').scan('A').scan('A').total() == 380
    }
}
