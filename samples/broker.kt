@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.brokers.*
import org.roboquant.common.Wallet
import org.roboquant.feeds.Event
import org.roboquant.orders.MarketOrder
import org.roboquant.orders.Order


fun usageBasic2(broker: Broker) {
    // tag::basic[]
    val orders = emptyList<Order>()
    val event = Event.empty()
    val account = broker.place(orders, event)
    // end::basic[]
}




fun account(broker: Broker) {
    // tag::account[]
    val account = broker.account

    // Standard Kotlin
    val winningTrades = account.trades.filter { it.pnl > 0 }
    val biggestPosition = account.positions.maxBy { it.exposure.value }
    val marketOrders = account.openOrders.filterIsInstance<MarketOrder>()
    val appleTrades = account.trades.filter { it.asset.symbol == "AAPL" }

    // Some useful extensions
    val totalUnrealizedPNL = account.positions.unrealizedPNL
    val totalRealizedPNL = account.trades.realizedPNL
    val totalFee = account.trades.fee
    val realizedPNLApple = appleTrades.realizedPNL
    // end::account[]
}

fun equity(account: Account, initialDeposit: Wallet) {
    // tag::equity[]
    // Sum of cash balances + open positions
    val equity1 = account.cash + account.positions.marketValue

    // Sum of initial deposit + all the Profit And Loss
    val equity2 = initialDeposit + account.trades.realizedPNL + account.positions.unrealizedPNL

    assert(equity1 == equity2)
    // end::equity[]
}
