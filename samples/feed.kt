@file:Suppress(
    "unused", "ControlFlowWithEmptyBody", "UNUSED_VARIABLE", "WildcardImport", "MagicNumber", "TooManyFunctions"
)

import org.roboquant.Roboquant
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.avro.AvroFeed
import org.roboquant.common.*
import org.roboquant.feeds.*
import org.roboquant.feeds.csv.CSVConfig
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.feeds.csv.LazyCSVFeed
import org.roboquant.feeds.csv.PriceBarParser
import org.roboquant.questdb.QuestDBFeed
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    println(feed.assets)
    // end::basic[]
}


fun aggr() {
    // tag::aggr[]
    val feed = CSVFeed("data/test_1min")
    val fifteenMinuteFeed = AggregatorFeed(feed, 15.minutes)
    // end::aggr[]
}



fun config() {
    // tag::config[]
    val df = DateTimeFormatter.ISO_DATE_TIME
    val config = CSVConfig(
        filePattern = "*.csv",
        fileSkip = listOf("skip_me.csv"),
        hasHeader = false,
        separator = ';',
        priceParser = PriceBarParser(
            autodetect = false,
            open = 1,
            close = 2,
            low = 3,
            high = 4,
            timeSpan = 15.minutes
        ),
        timeParser = { line, asset -> LocalDateTime.parse(line[0], df).atZone(asset.exchange.zoneId).toInstant() },
        assetBuilder = { file -> Asset(file.nameWithoutExtension) },
    )
    val feed = CSVFeed("somepath", config)
    // end::config[]
}

fun csvPreConfig() {
    // tag::csvpreconfig[]
    // CSV files downloaded from stooq.pl
    val feed1 = CSVFeed("somepath", CSVConfig.stooq())

    // CSV files downloaded from histdata.com
    val feed2 = CSVFeed("somepath", CSVConfig.histData())

    // CSV files downloaded from MT5
    val feed3 = CSVFeed("somepath", CSVConfig.mt5())

    // CSV trade files downloaded from Kraken
    val feed4 = CSVFeed("somepath", CSVConfig.kraken())

    // CSV files downloaded from Yahoo Finance
    val feed5 = CSVFeed("somepath", CSVConfig.yahoo())
    // end::csvpreconfig[]
}


fun testLazy(roboquant: Roboquant) {
    // tag::lazy[]
    val feed = LazyCSVFeed("data/test")
    roboquant.run(feed)
    // end::lazy[]
}


fun combinedFeed(roboquant: Roboquant) {
    // tag::combined[]
    val feed1 = CSVFeed("data/test1")
    val feed2 = CSVFeed("data/test2")
    val feed = CombinedFeed(feed1, feed2)

    roboquant.run(feed)
    // end::combined[]
}

fun play() {
    // tag::play[]
    suspend fun play(channel: EventChannel) {
        for (i in 1..100) {
            val actions = emptyList<Item>() // replace with a real list of actions
            val now = Instant.now()
            val event = Event(now, actions)
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

    val inputFeed = CSVFeed("some/path/")
    val outputFeed = AvroFeed("my_file.avro")
    outputFeed.record(inputFeed)

    // Append to existing avro file
    val inputFeed2 = CSVFeed("some/path2/")
    outputFeed.record(inputFeed, append = true)
    // end::avrocapture[]
}


fun avroCaptureLive() {
    // tag::avrocapturelive[]
    val feed = AlpacaLiveFeed()
    feed.subscribeStocks("AAPL", "TSLA", "IBM")
    var timeframe = Timeframe.next(4.hours)

    val avroFeed = AvroFeed("my_file.avro")
    avroFeed.record(feed, timeframe = timeframe)

    // You can also append to an existing Avro file
    timeframe = Timeframe.next(8.hours)
    avroFeed.record(feed, timeframe = timeframe, append = true)
    // end::avrocapturelive[]
}

private fun predefined() {
    // tag::predefined[]
    val feed1 = AvroFeed.sp25() // S&P 25 with daily PriceBar data
    // end::predefined[]
}

fun testEvent(event: Event) {
    // tag::event[]
    for (priceBar in event.items.filterIsInstance<PriceBar>()) {
        // your code
    }
    // end::event[]
}


fun questDB(inputFeed: Feed) {
    // tag::qdb[]
    val feed = QuestDBFeed("pricebars")
    feed.record<PriceBar>(inputFeed)

    println(feed.timeframe)
    // end::qdb[]
}


fun questDB2(inputFeed1: Feed, inputFeed2: Feed) {
    // tag::qdb2[]
    val feed = QuestDBFeed("prices")

    // If we want later to add out-of-order entries, we need to partition the table
    feed.record<PriceBar>(inputFeed1, partition = QuestDBFeed.Partition.YEAR)

    // Add the out-of-order pricebars
    feed.record<PriceBar>(inputFeed2, append = true)

    // end::qdb2[]
}

fun testPriceItem(event: Event) {
    // tag::PriceItem[]
    // iterate over all price actions in an event
    for (priceItem in event.prices) {
        println(priceItem)
    }

    // get the first price for a particular asset
    val price: Double? = event.getPrice(Asset("XYZ"))
    // end::PriceItem[]
}




