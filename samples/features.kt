@file:Suppress("unused", "MagicNumber")

import org.roboquant.Roboquant
import org.roboquant.brokers.Broker
import org.roboquant.common.Currency.Companion.USD
import org.roboquant.common.EUR
import org.roboquant.common.JPY
import org.roboquant.common.USD
import org.roboquant.common.sumOf
import org.roboquant.feeds.RandomWalkFeed
import org.roboquant.jupyter.TradeChart
import org.roboquant.loggers.SilentLogger
import org.roboquant.metrics.ProgressMetric
import org.roboquant.strategies.EMAStrategy
import kotlin.system.measureTimeMillis
import org.roboquant.ta.TaLibStrategy


private fun performance() {
    // tag::performance[]
    // Generate 1.008.000 1-minute price bars
    val feed = RandomWalkFeed.lastDays(days = 7, nAssets = 100)

    // Repeat three times to exclude compile time overhead of the first run
    repeat(3) {
        val t = measureTimeMillis {
            val roboquant = Roboquant(EMAStrategy(), ProgressMetric(), logger = SilentLogger())
            roboquant.run(feed)
        }
        println("time = $t ms")
    }
    // end::performance[]
}

private fun friendly1() {
    // tag::friendly1[]
    val wallet = 100.EUR + 20.USD + 1_000.JPY
    wallet.convert(USD)
    // end::friendly1[]
}

private fun strategy() {
    // tag::strategy[]
    val period = 10
    val strategy = TaLibStrategy()
    strategy.buy { it.high.last() > sma(it, period) }
    strategy.sell { it.low.last() < sma(it, period) }
    // end::strategy[]
}

private fun friendly2(broker: Broker) {
    // tag::friendly2[]
    val account = broker.account
    TradeChart(account.trades)
    // end::friendly2[]
}

private fun friendly3(broker: Broker) {
    // tag::friendly3[]
    val account = broker.account
    val totalPNL = account.trades.sumOf { it.pnl }
    val appleOrders = account.closedOrders.filter { it.asset.symbol == "AAPL" }
    val biggestPosition = account.positions.maxBy { it.marketValue.value }
    // end::friendly3[]
}
