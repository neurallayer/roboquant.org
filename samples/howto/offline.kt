package howto

import org.roboquant.Roboquant
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.common.Timeframe
import org.roboquant.common.hours
import org.roboquant.feeds.AvroFeed
import org.roboquant.feeds.csv.CSVFeed

fun csv2avro(roboquant: Roboquant) {

    val feed = CSVFeed("path/stocks")
    AvroFeed.record(feed, "stocks.avro")

    // Now use the avro feed
    val feed2 = AvroFeed("stocks.avro")
    roboquant.run(feed2)
}


fun live2avro(roboquant: Roboquant) {

    val feed = BinanceLiveFeed()
    feed.subscribePriceBar("BTCBUSD", "ETHBUSD")
    val timeframe = Timeframe.next(8.hours)
    AvroFeed.record(feed, "crypto.avro", timeframe = timeframe)

    // Now use the avro feed for back testing
    val feed2 = AvroFeed("crypto.avro")
    roboquant.run(feed2)
}