import org.roboquant.Roboquant
import org.roboquant.common.summary
import org.roboquant.feeds.Event
import org.roboquant.feeds.PriceBar
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.strategies.EMACrossover
import org.roboquant.strategies.Signal
import org.roboquant.strategies.Strategy

val roboquant = Roboquant(EMACrossover())

fun test() {
    // tag::basic[]
    val feed = CSVFeed("data/test")
    roboquant.run(feed)
    feed.assets.summary().log()
    // end::basic[]
}



fun testEvent(event: Event) {
    // tag::event[]
    for (priceBar in event.actions.filterIsInstance<PriceBar>()) {
        // your code
    }
    // end::event[]
}



