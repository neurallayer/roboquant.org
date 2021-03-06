@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")


import org.roboquant.Roboquant
import org.roboquant.common.Asset
import org.roboquant.common.max
import org.roboquant.common.min
import org.roboquant.feeds.Event
import org.roboquant.strategies.*
import org.roboquant.ta.TaLibStrategy

fun ema() {

    // tag::ema[]
    // Predefined EMACrossover
    val strategy1 = EMACrossover.EMA_12_26

    // Own defined look-back periods
    val strategy2 = EMACrossover(fastPeriod = 20, slowPeriod = 50)
    // end::ema[]

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

        override fun generate(asset: Asset, data: DoubleArray): Signal? {
            return when {
                data.last() == data.max() -> Signal(asset, Rating.BUY)
                data.last() == data.min() -> Signal(asset, Rating.SELL)
                else -> null
            }
        }

    }

    // end::extend[]
}


fun composition(strategy1: Strategy, strategy2: Strategy) {
    // tag::composition[]
    val strategy = CombinedStrategy(strategy1, strategy2)
    val roboquant = Roboquant(strategy)
    // end::composition[]
}


fun ta() {

    // tag::ta[]
    val shortTerm = 30
    val longTerm = 50

    // Make sure the TAStrategy collects enough data for the used indicators to work
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


// tag::basic[]
class MyStrategy : Strategy {

    override fun generate(event: Event): List<Signal> {
        TODO("Not yet implemented")
    }

}
// end::basic[]


// tag::naive[]
class MyStrategy2 : Strategy {

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


