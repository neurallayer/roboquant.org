@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")


import org.roboquant.RunPhase
import org.roboquant.common.Asset
import org.roboquant.common.max
import org.roboquant.common.mean
import org.roboquant.common.min
import org.roboquant.feeds.Event
import org.roboquant.strategies.*
import org.roboquant.ta.TAStrategy

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
                data.max() > data.mean() * 2 -> Signal(asset, Rating.BUY)
                data.min() > data.mean() / 2 -> Signal(asset, Rating.SELL)
                else -> null
            }
        }

    }

    // end::extend[]
}

fun ta() {

    // tag::ta[]
    val shortTerm = 30
    val longTerm = 50

    // Make sure the TAStrategy collects enough data for the used indicators to work
    val strategy = TAStrategy(longTerm)

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
            if (currentPrice > 1.05 * previousPrice) signals.add(Signal(asset, Rating.BUY))
            if (currentPrice < 0.95 * previousPrice) signals.add(Signal(asset, Rating.SELL))
            previousPrices[asset] = currentPrice
        }
        return signals
    }

    override fun start(runPhase: RunPhase) {
        previousPrices.clear()
    }

}
// end::naive[]


