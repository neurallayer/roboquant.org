@file:Suppress("unused", "ControlFlowWithEmptyBody", "UNUSED_VARIABLE" , "WildcardImport", "MagicNumber")

import org.roboquant.Roboquant
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.common.Asset
import org.roboquant.common.Timeframe
import org.roboquant.common.hours
import org.roboquant.common.summary
import org.roboquant.feeds.*
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.feeds.csv.LazyCSVFeed
import java.time.Instant


fun feedInterface() {
    // tag::interface[]
    class MYFeed : Feed {

        val timeFrame: Timeframe
            get() = Timeframe.INFINITE

        override suspend fun play(channel: EventChannel) {
            TODO() // Your code goes here
        }
    }
    // end::interface[]
}

fun test(roboquant: Roboquant) {
    // tag::basic[]
    val feed = CSVFeed("data/test")
    roboquant.run(feed)
    println(feed.assets.summary())
    // end::basic[]
}


fun csvPreConfig() {
    // tag::csvpreconfig[]
    // CSV files downloaded from stooq.pl
    val feed1 = CSVFeed.fromStooq("somepath")

    // CSV files downloaded from histdata.com
    val feed2 = CSVFeed.fromHistData("somepath")
    // end::csvpreconfig[]
}


fun testLazy(roboquant: Roboquant) {
    // tag::lazy[]
    val feed = LazyCSVFeed("data/test")
    roboquant.run(feed)
    // end::lazy[]
}


fun play() {
    // tag::play[]
    suspend fun play(channel: EventChannel) {
        for (i in 1..100) {
            val actions = emptyList<Action>() // replace with real list of actions
            val now = Instant.now()
            val event = Event(actions, now)
            channel.send(event)
        }
    }
    // end::play[]
}


fun avro(roboquant: Roboquant) {
    // tag::avro[]
    val feed = AvroFeed("my_file.avro")
    roboquant.run(feed)
    // end::avro[]
}


fun avroCapture() {
    // tag::avrocapture[]
    val fileName = "my_file.avro"

    val feed = CSVFeed("some/path/")
    AvroFeed.record(feed, fileName)

    // Append to existing avro file
    val feed2 = CSVFeed("some/path2/")
    AvroFeed.record(feed2, fileName, append = true)

    // Later you can use the same file in a AvroFeed
    val feed3 = AvroFeed(fileName)
    // end::avrocapture[]
}


fun avroCaptureLive() {
    // tag::avrocapturelive[]
    val feed = BinanceLiveFeed()
    feed.subscribePriceBar("BTCUSDT")
    var timeframe = Timeframe.next(4.hours)
    AvroFeed.record(feed, "bitcoin.avro", timeframe = timeframe)

    // You can also append to an existing Avro file
    timeframe = Timeframe.next(8.hours)
    AvroFeed.record(feed, "bitcoin.avro", timeframe = timeframe, append = true)
    // end::avrocapturelive[]
}

private fun predefined() {
    // tag::predefined[]
    val feed1 = AvroFeed.sp500() // S&P500 with daily PriceBar data
    val feed2 = AvroFeed.sp500Quotes() // S&P500 with PriceQuote data
    val feed3 = AvroFeed.forex() // EUR/USD forex data
    // end::predefined[]
}

fun testEvent(event: Event) {
    // tag::event[]
    for (priceBar in event.actions.filterIsInstance<PriceBar>()) {
        // your code
    }
    // end::event[]
}


fun testPriceAction(event: Event) {
    // tag::priceaction[]
    // iterate over all price actions in an event
    for (priceAction in event.prices) {
        println(priceAction)
    }

    // get the first price for a particular asset
    val price: Double? = event.getPrice(Asset("XYZ"))
    // end::priceaction[]
}




