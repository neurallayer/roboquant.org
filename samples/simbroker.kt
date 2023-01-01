@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.brokers.*
import org.roboquant.brokers.sim.*
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

fun configSimBroker() {
    // tag::extra[]
    val broker = SimBroker(
        initialDeposit = Wallet(10_000.EUR), // How much to initially deposit into the account
        baseCurrency = Currency.EUR, // Currency to use for reporting
        feeModel = PercentageFeeModel(), // Logic to use to calculate fees, commissions, etc
        accountModel = MarginAccount(), // Cash or Margin account
        pricingEngine = SpreadPricingEngine() // Engine to use to calculate the price of a trade
    )
    // end::extra[]
}


fun includedModels() {
    // tag::included[]
    // Account Models
    val accountModel1 = CashAccount(minimum = 1_000.0)
    val accountModel2 = MarginAccount(
        initialMargin = 0.5,
        maintenanceMarginLong = 0.3,
        maintenanceMarginShort = 0.2,
        minimumEquity = 10_000.0
    )

    // Fee models
    val feeModel1 = PercentageFeeModel(0.01) // 1% fee
    val feeModel2 = NoFeeModel()

    // Pricing engines
    val pricingEngine1 = SpreadPricingEngine(spreadInBips = 5, priceType = "OPEN")
    val pricingEngine2 = NoCostPricingEngine(priceType = "CLOSE")
    // end::included[]
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
