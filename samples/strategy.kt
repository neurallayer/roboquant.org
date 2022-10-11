@file:Suppress("unused", "UNUSED_VARIABLE")


import org.roboquant.Roboquant
import org.roboquant.common.Asset
import org.roboquant.common.max
import org.roboquant.common.min
import org.roboquant.feeds.Event
import org.roboquant.strategies.*
import org.roboquant.ta.TaLibStrategy

fun ema() {

    // tag::ema[]
    // Use a EMA Crossover Strategy with predefined look-back periods
    val strategy1 = EMAStrategy.EMA_12_26

    // Use a EMA Crossover Strategy custom look-back periods
    val strategy2 = EMAStrategy(fastPeriod = 20, slowPeriod = 50)
    // end::ema[]

}


fun simpleSignal() {
    // tag::simpleSignal[]
    val apple = Asset("AAPL")
    val signal = Signal(apple, Rating.BUY)
    // end::simpleSignal[]
}


@Suppress("RedundantExplicitType")
fun allSignalAttributes(asset: Asset, rating: Rating) {
    // tag::attrSignal[]
    val type: SignalType = SignalType.BOTH
    val takeProfit: Double = Double.NaN
    val stopLoss: Double = Double.NaN
    val probability: Double = Double.NaN
    val source: String = ""
    Signal(asset, rating, type, takeProfit, stopLoss, probability, source)
    // end::attrSignal[]
}

fun rsi() {

    // tag::rsi[]
    // Default thresholds values
    val strategy1 = RSIStrategy()

    // Own defined thresholds
    val strategy2 = RSIStrategy(lowThreshold = 25.0, highThreshold = 75.0)
    // end::rsi[]

}

fun extending() {
    // tag::extend[]
    class MyStrategy1(lookBack:Int= 10) : HistoricPriceStrategy(lookBack) {

        /**
         * this method should return a Signal or null. Signal can contain
         * additional attributes to the ones shown in this example.
         */
        override fun generateSignal(asset: Asset, data: DoubleArray): Signal? {
            return when {
                data.last() == data.max() -> Signal(asset, Rating.BUY)
                data.last() == data.min() -> Signal(asset, Rating.SELL)
                else -> null
            }
        }

    }

    class MyStrategy2(lookBack:Int= 10) : HistoricPriceStrategy(lookBack) {

        /**
         * this method should return a Rating or null.
         */
        override fun generateRating(data: DoubleArray): Rating? {
            return when {
                data.last() == data.max() -> Rating.BUY
                data.last() == data.min() -> Rating.SELL
                else -> null
            }
        }

    }

    // end::extend[]
}


fun composition(strategy1: Strategy, strategy2: Strategy, strategy3: Strategy) {
    // tag::composition[]
    val strategy = CombinedStrategy(strategy1, strategy2, strategy3)
    val roboquant = Roboquant(strategy)
    // end::composition[]
}


fun ta() {

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

fun customStrategy1() {
    // tag::basic[]
    class MyStrategy : Strategy {

        override fun generate(event: Event): List<Signal> {
            TODO("Not yet implemented")
        }

    }
// end::basic[]
}

fun customStrategy2() {
    // tag::naive[]
    class MyStrategy : Strategy {

        private val previousPrices = mutableMapOf<Asset, Double>()

        override fun generate(event: Event): List<Signal> {
            val signals = mutableListOf<Signal>()
            for ((asset, priceAction) in event.prices) {

                val currentPrice = priceAction.getPrice()
                val previousPrice = previousPrices.getOrDefault(asset, currentPrice)

                if (currentPrice > 1.05 * previousPrice)
                    signals.add(Signal(asset, Rating.BUY))

                if (currentPrice < 0.95 * previousPrice)
                    signals.add(Signal(asset, Rating.SELL))

                previousPrices[asset] = currentPrice
            }
            return signals
        }

        // Make sure we clear the previous prices when reset
        override fun reset() {
            previousPrices.clear()
        }

    }
    // end::naive[]
}


