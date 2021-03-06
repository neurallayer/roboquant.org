@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.brokers.Account
import org.roboquant.brokers.marketValue
import org.roboquant.brokers.realizedPNL
import org.roboquant.brokers.sim.MarginAccount
import org.roboquant.brokers.sim.PercentageFeeModel
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.brokers.sim.SpreadPricingEngine
import org.roboquant.brokers.unrealizedPNL
import org.roboquant.common.Currency
import org.roboquant.common.EUR
import org.roboquant.common.Wallet


fun usageBasic() {
    // tag::basic[]
    val broker = SimBroker()
    // end::basic[]
}


fun usageExtra() {
    // tag::extra[]
    val broker = SimBroker(
        initialDeposit = Wallet(10_000.EUR),
        baseCurrency = Currency.EUR,
        feeModel = PercentageFeeModel(),
        accountModel = MarginAccount(),
        pricingEngine = SpreadPricingEngine()
    )
    // end::extra[]
}


fun equity(account: Account, initialDeposit: Wallet) {
    // tag::equity[]

    // Current state
    val equity1 = account.cash + account.portfolio.marketValue

    // Sum of all changes
    val equity2 = initialDeposit + account.trades.realizedPNL + account.positions.unrealizedPNL

    assert(equity1 == equity2)
    // end::equity[]
}