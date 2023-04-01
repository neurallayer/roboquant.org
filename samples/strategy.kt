@file:Suppress("unused", "UNUSED_VARIABLE", "TooManyFunctions")


import org.roboquant.Roboquant
import org.roboquant.common.Asset
import org.roboquant.common.AssetFilter
import org.roboquant.common.Currency
import org.roboquant.feeds.Event
import org.roboquant.strategies.*
import org.roboquant.ta.*
import org.roboquant.ta.TaLibStrategy
import java.time.Instant


private fun intro() {

    class MyStrategy : Strategy {

        // tag::intro[]
        override fun generate(event: Event): List<Signal> {
        // end::intro[]
           TODO("Not yet implemented")
        }
    }
}

private fun ema() {
    // tag::ema[]
    // Use a EMA  crossover strategy with predefined periods
    val strategy1 = EMAStrategy.PERIODS_5_15
    val strategy2 = EMAStrategy.PERIODS_12_26
    val strategy3 = EMAStrategy.PERIODS_50_200

    // Use an EMA crossover strategy with custom periods
    val strategy4 = EMAStrategy(fastPeriod = 20, slowPeriod = 50)
    // end::ema[]
}


private fun simpleSignal() {
    // tag::simpleSignal[]
    val apple = Asset("AAPL")
    val signal = Signal(apple, Rating.BUY)
    // end::simpleSignal[]
}



private fun filter() {
    // tag::filter[]
    // Only receive price actions denoted in USD
    val strategy1 = AssetFilterStrategy(EMAStrategy(), AssetFilter.includeCurrencies(Currency.USD))

    // use the filter extension function to exclude price actions for Tesla
    val strategy2 = EMAStrategy().filter { asset, time ->
        asset.symbol != "TSLA"
    }

    // use CombinedStrategy and filter to create more complex strategies
    val t = Instant.parse("2010-01-01T00:00:00Z")
    val strategy = CombinedStrategy(
        EMAStrategy.PERIODS_12_26.filter { asset, time -> time <  t },
        EMAStrategy.PERIODS_5_15.filter { asset, time -> time >=  t }
    )
    // end::filter[]
}



@Suppress("RedundantExplicitType")
private fun allSignalAttributes(asset: Asset, rating: Rating) {
    // tag::attrSignal[]
    // Type of signal: ENTRY, EXIT or BOTH
    val type: SignalType = SignalType.BOTH

    // The price when to exit when profitable
    val takeProfit: Double = Double.NaN

    // The price when to exit when unprofitable
    val stopLoss: Double = Double.NaN

    // The probability of this signal being correct
    val probability: Double = Double.NaN

    // An arbitrary string, for example to trace the strategy that generated this signal
    val tag: String = ""

    Signal(asset, rating, type, takeProfit, stopLoss, probability, tag)
    // end::attrSignal[]
}

private fun rsi() {

    // tag::rsi[]
    // Default thresholds values
    val strategy1 = RSIStrategy()

    // Own defined thresholds
    val strategy2 = RSIStrategy(lowThreshold = 25.0, highThreshold = 75.0)
    // end::rsi[]

}

private fun extending() {
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


private fun composition(strategy1: Strategy, strategy2: Strategy, strategy3: Strategy) {
    // tag::composition[]
    val strategy = CombinedStrategy(strategy1, strategy2, strategy3)
    val roboquant = Roboquant(strategy)
    // end::composition[]


}


private fun composition2(strategy1: Strategy, strategy2: Strategy, strategy3: Strategy) {
    // tag::parallel[]
    val strategy = ParallelStrategy(strategy1, strategy2, strategy3)
    val roboquant = Roboquant(strategy)
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

        override fun generate(event: Event): List<Signal> {
            TODO("Not yet implemented")
        }

    }
    // end::basic[]
}

private fun customStrategy2() {
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


