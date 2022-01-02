import org.roboquant.brokers.Account
import org.roboquant.feeds.Event
import org.roboquant.orders.*
import org.roboquant.policies.DefaultPolicy
import org.roboquant.policies.Policy
import org.roboquant.strategies.Signal

// tag::basic[]
class MyPolicy : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val orders = mutableListOf<Order>()
        // Your code here
        return orders
    }
}
// end::basic[]


// tag::default[]
class MyDefaultPolicy(private val percentage:Double = 0.05) : DefaultPolicy() {

    override fun createOrder(signal: Signal, qty: Double, price: Double): Order? {
        // We don't short and  all other sell/exit orders are covered by the bracket order
        if (qty < 0) return null

        val asset = signal.asset
        return BracketOrder(
            MarketOrder(asset, qty),
            TrailOrder(asset, -qty, percentage/2.0),
            StopOrder(asset, -qty, price* (1 - percentage))
        )
    }
}
// end::default[]


// tag::naive[]
class MyNativePolicy : Policy {

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
class MyNativePolicy2 : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        return signals.map {
            val qty = if (it.rating.isPositive) 100.0 else -100.0
            MarketOrder(it.asset, qty)
        }
    }
}
// end::naive2[]