@file:Suppress("unused")

import org.roboquant.Roboquant
import org.roboquant.brokers.Broker
import org.roboquant.common.Currency.Companion.USD
import org.roboquant.common.EUR
import org.roboquant.common.JPY
import org.roboquant.common.USD
import org.roboquant.feeds.RandomWalkFeed
import org.roboquant.jupyter.TradeChart
import org.roboquant.loggers.SilentLogger
import org.roboquant.metrics.ProgressMetric
import org.roboquant.strategies.EMAStrategy
import kotlin.system.measureTimeMillis


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


private fun friendly2(broker: Broker) {
    // tag::friendly2[]
    val account = broker.account
    TradeChart(account.trades)
    // end::friendly2[]
}
