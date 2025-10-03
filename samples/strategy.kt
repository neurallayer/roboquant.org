@file:Suppress("unused", "UNUSED_VARIABLE", "TooManyFunctions", "WildcardImport", "MagicNumber")

import org.roboquant.common.*
import org.roboquant.feeds.HistoricFeed
import org.roboquant.feeds.LiveFeed
import org.roboquant.run
import org.roboquant.strategies.*
import org.roboquant.ta.TaLibStrategy
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


private fun intro() {

    class MyStrategy : Strategy {

        // tag::intro[]
        override fun createSignals(event: Event): List<Signal> {
            TODO("Not yet implemented")
        }
        // end::intro[]
    }
}

private fun ema() {
    // tag::ema[]
    // Use an EMA-crossover strategy with predefined periods
    val strategy1 = EMACrossover.PERIODS_5_15
    val strategy2 = EMACrossover.PERIODS_12_26
    val strategy3 = EMACrossover.PERIODS_50_200

    // Use an EMA crossover strategy with custom periods
    val strategy4 = EMACrossover(fastPeriod = 20, slowPeriod = 50)
    // end::ema[]
}


private fun simpleSignal() {
    // tag::simpleSignal[]
    val apple = Stock("AAPL")
    val signal = Signal.buy(apple)
    // end::simpleSignal[]
}


@Suppress("RedundantExplicitType")
private fun allSignalAttributes(asset: Asset) {
    // tag::attrSignal[]
    // Type of signal: ENTRY, EXIT or BOTH
    val type: SignalType = SignalType.BOTH

    // An arbitrary string, for example, to trace the strategy that generated this signal
    val tag: String = ""

    // A double value
    val rating = 1.0

    Signal(asset, rating, type, tag)
    // end::attrSignal[]
}

private fun extending() {
    // tag::extend[]
    class MyStrategy1(lookBack: Int = 10) : HistoricPriceStrategy(lookBack) {

        /**
         * This method should return a [Signal] or null. A signal can contain
         * additional attributes to the ones shown in this example.
         */
        override fun generateSignal(asset: Asset, data: DoubleArray): Signal? {
            return when {
                data.last() == data.maxOrNull() -> Signal.buy(asset)
                data.last() == data.minOrNull() -> Signal.sell(asset)
                else -> null
            }
        }

    }

    class MyStrategy2(lookBack: Int = 10) : HistoricPriceStrategy(lookBack) {

        /**
         * This method should return a rating value or null.
         */
        override fun generateRating(data: DoubleArray): Double? {
            return when {
                data.last() == data.maxOrNull() -> 1.0
                data.last() == data.minOrNull() -> -1.0
                else -> null
            }
        }

    }
    // end::extend[]
}


private fun composition(strategy1: Strategy, strategy2: Strategy, strategy3: Strategy) {
    // tag::composition[]
    val strategy = CombinedStrategy(strategy1, strategy2, strategy3)
    // end::composition[]
}


private fun composition2(strategy1: Strategy, strategy2: Strategy, strategy3: Strategy) {
    // tag::parallel[]
    val strategy = ParallelStrategy(strategy1, strategy2, strategy3)
    // end::parallel[]
}


private fun ta() {

    // tag::ta[]
    val shortTerm = 30
    val longTerm = 50

    // Make sure the strategy collects enough data
    // for all the used indicators to work correctly
    val strategy = TaLibStrategy(longTerm)

    // When to generate a BUY signal
    strategy.buy { series ->
        cdlMorningStar(series) || cdl3WhiteSoldiers(series)
    }

    // When to generate a SELL signal
    strategy.sell { series ->
        cdl3BlackCrows(series) || (cdl2Crows(series) &&
                sma(series.close, shortTerm) < sma(series.close, longTerm))
    }
    // end::ta[]

}

private fun customStrategy1() {
    // tag::basic[]
    class MyStrategy : Strategy {

        override fun createSignals(event: Event):List<Signal> {
            TODO("Not yet implemented")
        }

    }
    // end::basic[]
}

private fun customStrategy2() {
    // tag::naive[]
    class MyStrategy : Strategy {

        private val previousPrices = mutableMapOf<Asset, Double>()

        override fun createSignals(event: Event): List<Signal> {
            val signals = mutableListOf<Signal>()
            for ((asset, priceItem) in event.prices) {

                val currentPrice = priceItem.getPrice()
                val previousPrice = previousPrices.getOrDefault(asset, currentPrice)

                if (currentPrice > 1.05 * previousPrice)
                    signals.add(Signal.buy(asset))

                if (currentPrice < 0.95 * previousPrice)
                    signals.add(Signal.sell(asset))

                previousPrices[asset] = currentPrice
            }
            return signals
        }

    }
    // end::naive[]
}


fun warmup(liveFeed: LiveFeed, historicFeed: HistoricFeed) {
    // tag::warmup[]
    val strategy = EMACrossover()

    // Run a warmup over the past 30 days with the default SimBroker, so no live orders are generated
    val past = Timeframe.past(30.days)
    run(historicFeed, strategy, timeframe = past)

    // Start the live trading with a real broker over the next 8 hours, now the strategy is warmed-up
    val next = Timeframe.next(8.hours)
    run(liveFeed, strategy, timeframe = next)
    // end::warmup[]
}
