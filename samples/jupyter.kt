@file:Suppress("unused", "UNUSED_PARAMETER")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.*
import org.roboquant.feeds.Feed
import org.roboquant.feeds.HistoricFeed
import org.roboquant.jupyter.*

fun use1(data: TimeSeries) {
    // tag::use1[]
    // If last line, will get plotted
    MetricChart(data)
    // end::use1[]
}

fun use2(feed: HistoricFeed, assets: List<Asset>) {
    // tag::use2[]
    // Plot the correlation matrix over different timeframes
    for (timeframe in Timeframe.past(20.years).split(2.years))
        PriceCorrelationChart(feed, assets, timeframe).render()
    // end::use2[]
}


fun overview2(data: TimeSeries, account: Account, feed: Feed, asset: Asset, assets: List<Asset>) {
    // tag::overview[]
    // Metric related charts
    MetricBoxChart(data)
    MetricHistogramChart(data)
    MetricCalendarChart(data)

    // Price related charts
    PriceChart(feed, asset)
    PriceBarChart(feed, asset)
    PriceCorrelationChart(feed, assets)

    // Asset related charts
    AssetAllocationChart(account.positions)
    AssetPerformanceChart(feed)

    // Trade related charts
    TradeChart(account.trades)
    TradeAssetChart(account.trades)

    // Order related chart
    OrderChart(account.openOrders)
    // end::overview[]
}

fun global() {
    // tag::global[]
    Chart.maxSamples = 100_000 // Max samples to use when drawing a chart
    // end::global[]
}


fun priceBarChart(feed: HistoricFeed, roboquant: Roboquant) {
    // tag::priceBarChart[]
    val trades = roboquant.broker.account.trades

    // Plot the prices of the first asset from a historic feed and also all the trades for that same asset
    PriceBarChart(feed, feed.assets.first(), trades = trades)
    // end::priceBarChart[]
}


fun priceChart(feed: HistoricFeed, roboquant: Roboquant) {
    // tag::priceChart[]
    // Plot how Apple performed during the 2008 financial crisis
    val apple = feed.assets.getBySymbol("AAPL")
    PriceChart(feed, apple, timeframe = Timeframe.financialCrisis2008)
    // end::priceChart[]
}


fun correlation(feed: HistoricFeed, roboquant: Roboquant) {
    // tag::correlation[]
    val assets = feed.assets.take(10)
    PriceCorrelationChart(feed, assets, timeframe = Timeframe.past(2.years))
    // end::correlation[]
}


