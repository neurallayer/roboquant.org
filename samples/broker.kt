@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.brokers.*
import org.roboquant.common.Asset
import org.roboquant.common.Currency
import org.roboquant.common.Wallet
import org.roboquant.orders.MarketOrder
import org.roboquant.orders.cancel
import java.time.ZoneId
import java.time.temporal.ChronoField


fun placeOrder(broker: Broker) {
    // tag::basic[]
    val orders = listOf(
        MarketOrder(Asset("AAPL"), 100),
        MarketOrder(Asset("TSLA"), -100),
    )
    val account = broker.place(orders)
    // end::basic[]
}


fun account(broker: Broker) {
    // tag::account[]
    val account = broker.account

    // Print the account
    println(account.summary())
    println(account.fullSummary())

    // Standard Kotlin
    val winningTrades = account.trades.filter { it.pnl > 0 }
    val loosingTrades = account.trades.filter { it.pnl < 0 }
    val biggestExposure = account.positions.maxBy { it.exposure.value }
    val marketOrders = account.openOrders.filter { it.order is MarketOrder}
    val appleTrades = account.trades.filter { it.asset.symbol == "AAPL" }

    // Some useful extensions
    val totalUnrealizedPNL = account.positions.unrealizedPNL
    val totalRealizedPNL = account.trades.realizedPNL
    val totalRealizedLosses = loosingTrades.realizedPNL
    val biggestLoosingTrade = account.trades.map { it.pnl.convert(Currency.USD) }.minBy { it.value }
    val totalFee = account.trades.fee
    val realizedPNLApple = appleTrades.realizedPNL
    val cancellationOrders = account.openOrders.cancel()

    // More advanced examples
    val zone = ZoneId.systemDefault()
    val realizedPNLPerDayOfWeek = account.trades
        .groupBy { it.time.atZone(zone).get(ChronoField.DAY_OF_WEEK) }
        .mapValues { it.value.realizedPNL }
    val maxFeeExchange = account.trades
        .groupBy { it.asset.exchange }
        .mapValues { it.value.fee.convert(Currency.USD) }
        .maxBy { it.value.value }
    // end::account[]
}

fun equity(account: Account, initialDeposit: Wallet) {
    // tag::equity[]
    // Equity property
    val equity0 = account.equity

    // Sum of cash balances + open positions
    val equity1 = account.cash + account.positions.marketValue

    // Sum of initial deposit + all the Profit And Loss
    val equity2 = initialDeposit + account.trades.realizedPNL + account.positions.unrealizedPNL

    assert(equity0 == equity1)
    assert(equity0 == equity2)
    // end::equity[]
}
