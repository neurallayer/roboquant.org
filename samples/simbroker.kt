@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.brokers.*
import org.roboquant.brokers.sim.MarginAccount
import org.roboquant.brokers.sim.PercentageFeeModel
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.brokers.sim.SpreadPricingEngine
import org.roboquant.common.Currency
import org.roboquant.common.EUR
import org.roboquant.common.Wallet
import org.roboquant.feeds.Event
import org.roboquant.orders.Order


fun usageBasic() {
    // tag::basic[]
    val broker = SimBroker()
    // end::basic[]
}


fun usageBasic2(broker: Broker) {
    // tag::basic2[]
    val orders = emptyList<Order>()
    val event = Event.empty()
    val account = broker.place(orders, event)
    // end::basic2[]
}

fun usageExtra() {
    // tag::extra[]
    val broker = SimBroker(
        initialDeposit = Wallet(10_000.EUR), // How much to initially deposit into account
        baseCurrency = Currency.EUR, // Currency to use for reporting
        feeModel = PercentageFeeModel(), // Logic to use to calculate fees, commissions, etc
        accountModel = MarginAccount(), // Cash or Margin account
        pricingEngine = SpreadPricingEngine() // Logic to use to determine the price for a trade
    )
    // end::extra[]
}


fun equity(account: Account, initialDeposit: Wallet) {
    // tag::equity[]
    // Sum of cash balances + open positions
    val equity1 = account.cash + account.positions.marketValue

    // Sum of initial deposit + all the changes
    val equity2 = initialDeposit + account.trades.realizedPNL + account.positions.unrealizedPNL

    assert(equity1 == equity2)
    // end::equity[]
}
