import org.roboquant.RunPhase
import org.roboquant.common.Asset
import org.roboquant.feeds.Event
import org.roboquant.strategies.Rating
import org.roboquant.strategies.Signal
import org.roboquant.strategies.Strategy


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
            val previousPrice = previousPrices.getOrDefault(asset, Double.MAX_VALUE)
            val currentPrice = priceAction.getPrice()
            if (currentPrice > previousPrice) signals.add(Signal(asset, Rating.BUY))
            previousPrices[asset] = currentPrice
        }
        return signals
    }

    override fun start(runPhase: RunPhase) {
        previousPrices.clear()
    }

}
// end::naive[]


