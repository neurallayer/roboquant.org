@file:Suppress("unused", "UNUSED_VARIABLE", "TooManyFunctions", "WildcardImport", "MagicNumber")

import org.roboquant.common.Asset
import org.roboquant.feeds.PriceBar
import org.roboquant.ta.*


private fun basicTaLib() {
    // tag::basic[]
    val taLib = TaLib()
    val asset = Asset("TEST")

    // Keep maximum 30 price-bars in history
    val serie = PriceBarSeries(30)
    repeat(30) {
        serie.add(PriceBar(asset, 100.0, 101.0, 99.0, 100.0, 1000))
    }

    // Calculate EMA over the last 10 days using the default price-type (close)
    val ema10: Double = taLib.ema(serie, 10)

    // Calculate EMA over the last 5 days using the low-prices of the price-bars
    val ema5: Double = taLib.ema(serie.low, 5)

    // Calculate RSI over the default period (14) using the default price-type (close)
    val rsi: Double = taLib.rsi(serie)

    // Did we detect the Two Crow candlestick pattern today?
    val found: Boolean = taLib.cdl2Crows(serie)

    // Did we detect the Two Crow candlestick pattern 4 days ago?
    val found2: Boolean = taLib.cdl2Crows(serie, 4)

    // Some indicators return multiple double values, like the Bollinger Bands
    val (high, mid, low) = taLib.bbands(serie)
    // end::basic[]
}


private fun dynamic(series: PriceBarSeries) {

    val taLib = TaLib()
    // tag::dynamic[]
    if (series.isFull()) {
        try {
            taLib.sma(series, 30)
        } catch (err: InsufficientData) {
            // The error contains the minimum required size
            series.increaseCapacity(err.minSize)
        }
    }
    // end::dynamic[]
}


private fun strategy() {
    // tag::strategy[]
    val strategy = TaLibStrategy()

    // Define the buy rule
    strategy.buy {
        cdl3StarsInSouth(it) || cdl3WhiteSoldiers(it)
    }

    // Define the sell rule
    strategy.sell {
        ema(it.close, 5) < ema(it.close, 10)
    }
    // end::strategy[]

}


private fun strategy2() {
    // tag::strategy2[]
    fun TaLib.myIndicator(period: Int = 10, series: PriceBarSeries): Double {
        val x1 = sma(series, period)
        val x2 = ema(series, period)
        return x1 - x2
    }

    val s = TaLibStrategy()
    s.buy {
        myIndicator(10, it) > 0
    }
    // end::strategy2[]

}


private fun strategy3() {
    // tag::strategy3[]
    fun TaLib.myIndicator(serie1M: PriceBarSeries): Double {
        val series5M = serie1M.aggregate(5)
        val x1 = sma(serie1M, 20)
        val x2 = ema(series5M, 2)
        return x1 - x2
    }

    // You have to specify enough history for both 1M and 5M
    val s = TaLibStrategy(21)
    s.buy {
        myIndicator(it) > 0
    }
    // end::strategy3[]
}


private fun metric() {
    // tag::metric[]
    val metric = TaLibMetric {
        val (h, m, l) = bbands(it)
        mapOf(
            "bbands.low" to l,
            "bbands.mid" to m,
            "bbands.high" to h,
        )
    }
    // end::metric[]

}


private fun indicator() {
    // tag::indicator[]
    val metric = TaLibIndicator {
        val value = if (cdl3WhiteSoldiers(it)) 1.0 else 0.0
        mapOf(
            "threewhitesoldiers" to value,
        )
    }
    // end::indicator[]

}
