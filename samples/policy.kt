import org.roboquant.brokers.Account
import org.roboquant.feeds.Event
import org.roboquant.orders.MarketOrder
import org.roboquant.orders.Order
import org.roboquant.policies.Policy
import org.roboquant.strategies.Signal

// tag::basic[]
class MyPolicy : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val result = mutableListOf<Order>()
        // Your code here
        return result
    }
}
// end::basic[]

// tag::naive[]
class MyNativePolicy :  Policy  {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val orders = mutableListOf<Order>()
        for (signal in signals) {
            val qty = if (signal.rating.isPositive) 100.0 else -100.0
            val order = MarketOrder(signal.asset, qty)
            orders.add(order)
        }
        return orders
    }
}
// end::naive[]

// tag::naive2[]
class MyNativePolicy2 :  Policy  {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        return signals.map {
            val qty = if (it.rating.isPositive) 100.0 else -100.0
            MarketOrder(it.asset, qty)
        }
    }
}
// end::naive2[]