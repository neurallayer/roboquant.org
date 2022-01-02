import org.roboquant.Roboquant
import org.roboquant.common.summary
import org.roboquant.feeds.Action
import org.roboquant.feeds.Event
import org.roboquant.feeds.EventChannel
import org.roboquant.feeds.PriceBar
import org.roboquant.feeds.avro.AvroFeed
import org.roboquant.feeds.avro.AvroUtil
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.strategies.EMACrossover
import java.time.Instant

val roboquant = Roboquant(EMACrossover())

fun test() {
    // tag::basic[]
    val feed = CSVFeed("data/test")
    roboquant.run(feed)
    feed.assets.summary().log()
    // end::basic[]
}



fun play() {
    // tag::play[]
    suspend fun play(channel: EventChannel) {
        for (i in 1..100) {
            val actions = listOf<Action>() // replace with real actions
            val now = Instant.now()
            val event = Event(actions, now)
            channel.send(event)
        }
    }
    // end::play[]
}


fun avro() {
    // tag::avro[]
    val feed = AvroFeed("myfile.avro")
    // end::avro[]
}


fun avroCapture() {
    // tag::avrocapture[]
    val feed = CSVFeed("some/path/")
    AvroUtil.record(feed, "sp500.avro")
    // end::avrocapture[]

}

fun testEvent(event: Event) {
    // tag::event[]
    for (priceBar in event.actions.filterIsInstance<PriceBar>()) {
        // your code
    }
    // end::event[]
}




