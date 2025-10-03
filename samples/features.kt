@file:Suppress("unused", "MagicNumber")


import org.roboquant.common.Currency.Companion.USD
import org.roboquant.common.EUR
import org.roboquant.common.JPY
import org.roboquant.common.USD
import org.roboquant.feeds.random.RandomWalk
import org.roboquant.journals.BasicJournal
import org.roboquant.run
import org.roboquant.strategies.EMACrossover
import org.roboquant.ta.TaLibStrategy
import java.time.Instant
import kotlin.system.measureTimeMillis


private fun performance() {
    // tag::performance[]
    // Generate 1.008.000 1-minute price bars
    val feed = RandomWalk.lastDays(days = 7, nAssets = 100)

    // Repeat three times to exclude compile time overhead of the first run
    repeat(3) {
        val t = measureTimeMillis {
            run(feed, EMACrossover(), journal = BasicJournal())
        }
        println("time = $t ms")
    }
    // end::performance[]
}

private fun friendly1() {
    // tag::friendly1[]
    val wallet = 100.EUR + 20.USD + 1_000.JPY
    wallet.convert(USD, Instant.now())
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

