@file:Suppress("unused", "UNUSED_VARIABLE", "WildcardImport")

import org.roboquant.Roboquant
import org.roboquant.binance.BinanceBroker
import org.roboquant.brokers.*
import org.roboquant.common.*
import org.roboquant.metrics.AccountMetric
import org.roboquant.orders.MarketOrder
import org.roboquant.orders.cancel
import org.roboquant.strategies.EMAStrategy
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoField


fun placeOrder(broker: Broker) {
    // tag::basic[]
    val orders = listOf(
        MarketOrder(Asset("AAPL"), 100), // BUY 100 stocks Apple
        MarketOrder(Asset("TSLA"), -100), // SELL 100 stocks Tesla
    )
    val account = broker.place(orders)
    // end::basic[]
}


fun positionTesla() {
    // tag::positionTesla[]
    val asset = Asset("TSLA")
    // We bought 5 stocks tesla
    // We bought at $250.10
    // The current market price is $250.05
    val position = Position(asset, Size(5), 250.10, 250.05, Instant.now())

    println(position.unrealizedPNL) // USD -0.25
    println(position.marketValue) // USD 1,250.25
    println(position.totalCost) // USD 1,250.50
    // end::positionTesla[]
}

fun positionBTC() {
    // tag::positionBTC[]
    val asset = Asset("BTC-USDT", AssetType.CRYPTO, "USDT", "CRYPTO")

    // We bought 0.1 bitcoin
    // We bought at 25.100 USDT
    // The current market price is 25.000 USDT (for 1 bitcoin)
    val position = Position(asset, Size(0.1), 25_100.0, 25_000.0, Instant.now())

    println(position.unrealizedPNL) // USDT -10.00
    println(position.marketValue) // USDT 2,500.00
    println(position.totalCost) // USDT 2,510.00
    // end::positionBTC[]
}

fun regular() {
    // tag::regular[]
    val roboquant = Roboquant(EMAStrategy(), AccountMetric(), broker = BinanceBroker())
    println(roboquant.broker.account.fullSummary())
    // end::regular[]
}

fun account(broker: Broker) {
    // tag::account[]
    val account = broker.account

    // Print the account
    println(account.summary())
    println(account.fullSummary())

    // Standard Kotlin
    val winningTrades = account.trades.filter { it.pnl > 0 }
    val loosingTrades = account.trades.filter { it.pnl < 0 }
    val biggestExposure = account.positions.maxBy { it.exposure.value }
    val marketOrders = account.openOrders.filter { it.order is MarketOrder}
    val appleTrades = account.trades.filter { it.asset.symbol == "AAPL" }

    // Some useful extensions
    val totalUnrealizedPNL = account.positions.unrealizedPNL
    val totalRealizedPNL = account.trades.realizedPNL
    val totalRealizedLosses = loosingTrades.realizedPNL
    val biggestLoosingTrade = account.trades.map { it.pnl.convert(Currency.USD) }.minBy { it.value }
    val totalFee = account.trades.fee
    val realizedPNLApple = appleTrades.realizedPNL
    val cancellationOrders = account.openOrders.cancel()

    // More advanced examples
    val zone = ZoneId.systemDefault()
    val realizedPNLPerDayOfWeek = account.trades
        .groupBy { it.time.atZone(zone).get(ChronoField.DAY_OF_WEEK) }
        .mapValues { it.value.realizedPNL }
    val maxFeeExchange = account.trades
        .groupBy { it.asset.exchange }
        .mapValues { it.value.fee.convert(Currency.USD) }
        .maxBy { it.value.value }
    // end::account[]
}

fun equity(account: Account, initialDeposit: Wallet) {
    // tag::equity[]
    // Equity property
    val equity0 = account.equity

    // Sum of cash balances + open positions
    val equity1 = account.cash + account.positions.marketValue

    // Sum of initial deposit + all the Profit And Loss
    val equity2 = initialDeposit + account.trades.realizedPNL + account.positions.unrealizedPNL

    assert(equity0 == equity1)
    assert(equity0 == equity2)
    // end::equity[]
}


const val SUMMARY_EXAMPLE = """
// tag::fullsummary[]    
account
├── last update: 2023-01-09T14:47:01Z
├── base currency: BUSD
├── cash: BUSD 199374.52
├── buying power: BUSD 199374.52
├── equity: BUSD 999175.51
├── position summary
│   ├── open: 4
│   ├── long: 4
│   ├── short: 0
│   ├── total value: BUSD 799800.99
│   ├── long value: BUSD 799800.99
│   ├── short value: BUSD 0.00
│   └── unrealized p&l: BUSD 1085.35
├── trade summary
│   ├── total: 20
│   ├── realized p&l: BUSD -1909.83
│   ├── fee:
│   ├── first: 2023-01-09T14:03:00.170064Z
│   ├── last: 2023-01-09T14:43:00.149166Z
│   ├── winners: 2
│   └── loosers: 6
├── order summary
│   ├── open: 0
│   ├── closed: 20
│   ├── completed: 20
│   ├── cancelled: 0
│   ├── expired: 0
│   └── rejected: 0
├── cash
│   ├──   ccy│    amount│
│   └──  BUSD│ 199374.52│
├── positions
│   ├──   symbol│  ccy│       size│ entry price│ mkt price│ mkt value│ unrlzd p&l│
│   ├──  XRPBUSD│ BUSD│  567419.08│        0.35│      0.35│ 200128.71│     433.97│
│   ├──  BNBBUSD│ BUSD│     714.96│      279.33│    279.80│ 200045.81│     337.51│
│   ├──  TRXBUSD│ BUSD│ 3630666.41│        0.05│      0.06│ 199831.88│     270.50│
│   └──  ETHBUSD│ BUSD│     150.81│     1324.52│   1324.81│ 199794.60│      43.37│
├── open orders
│   └── EMPTY
├── closed orders
│   ├──   type│  symbol│  ccy│    status│  id│            opened at│            closed at│                  details│
│   ├── MARKET│ TRXBUSD│ BUSD│ COMPLETED│ 336│ 2023-01-09T14:02:00Z│ 2023-01-09T14:03:00Z│  size=3640997.63 tif=GTC│
│   ├── MARKET│ XRPBUSD│ BUSD│ COMPLETED│ 337│ 2023-01-09T14:03:00Z│ 2023-01-09T14:04:00Z│   size=571265.35 tif=GTC│
│   ├── MARKET│ BNBBUSD│ BUSD│ COMPLETED│ 338│ 2023-01-09T14:04:00Z│ 2023-01-09T14:05:00Z│      size=719.66 tif=GTC│
│   ├── MARKET│ BNBBUSD│ BUSD│ COMPLETED│ 339│ 2023-01-09T14:06:00Z│ 2023-01-09T14:07:00Z│     size=-719.66 tif=GTC│
│   ├── MARKET│ XRPBUSD│ BUSD│ COMPLETED│ 340│ 2023-01-09T14:06:00Z│ 2023-01-09T14:07:00Z│  size=-571265.35 tif=GTC│
│   ├── MARKET│ TRXBUSD│ BUSD│ COMPLETED│ 341│ 2023-01-09T14:06:01Z│ 2023-01-09T14:07:00Z│ size=-3640997.63 tif=GTC│
│   ├── MARKET│ ETHBUSD│ BUSD│ COMPLETED│ 342│ 2023-01-09T14:07:00Z│ 2023-01-09T14:08:00Z│     size=-151.51 tif=GTC│
│   ├── MARKET│ BTCBUSD│ BUSD│ COMPLETED│ 343│ 2023-01-09T14:07:00Z│ 2023-01-09T14:08:00Z│      size=-11.59 tif=GTC│
│   ├── MARKET│ ETHBUSD│ BUSD│ COMPLETED│ 344│ 2023-01-09T14:11:00Z│ 2023-01-09T14:12:00Z│      size=151.51 tif=GTC│
│   ├── MARKET│ BTCBUSD│ BUSD│ COMPLETED│ 345│ 2023-01-09T14:11:00Z│ 2023-01-09T14:12:00Z│       size=11.59 tif=GTC│
│   ├── MARKET│ BNBBUSD│ BUSD│ COMPLETED│ 346│ 2023-01-09T14:12:00Z│ 2023-01-09T14:13:00Z│      size=717.63 tif=GTC│
│   ├── MARKET│ XRPBUSD│ BUSD│ COMPLETED│ 347│ 2023-01-09T14:12:00Z│ 2023-01-09T14:13:00Z│   size=569823.99 tif=GTC│
│   ├── MARKET│ TRXBUSD│ BUSD│ COMPLETED│ 348│ 2023-01-09T14:18:01Z│ 2023-01-09T14:19:00Z│  size=3637915.47 tif=GTC│
│   ├── MARKET│ TRXBUSD│ BUSD│ COMPLETED│ 349│ 2023-01-09T14:36:01Z│ 2023-01-09T14:37:00Z│ size=-3637915.47 tif=GTC│
│   ├── MARKET│ XRPBUSD│ BUSD│ COMPLETED│ 350│ 2023-01-09T14:37:00Z│ 2023-01-09T14:38:00Z│  size=-569823.99 tif=GTC│
│   ├── MARKET│ BNBBUSD│ BUSD│ COMPLETED│ 351│ 2023-01-09T14:38:00Z│ 2023-01-09T14:39:00Z│     size=-717.63 tif=GTC│
│   ├── MARKET│ XRPBUSD│ BUSD│ COMPLETED│ 352│ 2023-01-09T14:40:00Z│ 2023-01-09T14:41:00Z│   size=567419.08 tif=GTC│
│   ├── MARKET│ BNBBUSD│ BUSD│ COMPLETED│ 353│ 2023-01-09T14:41:00Z│ 2023-01-09T14:42:00Z│      size=714.96 tif=GTC│
│   ├── MARKET│ TRXBUSD│ BUSD│ COMPLETED│ 354│ 2023-01-09T14:41:01Z│ 2023-01-09T14:42:00Z│  size=3630666.41 tif=GTC│
│   └── MARKET│ ETHBUSD│ BUSD│ COMPLETED│ 355│ 2023-01-09T14:42:00Z│ 2023-01-09T14:43:00Z│      size=150.81 tif=GTC│
└── trades
    ├──                  time│  symbol│  ccy│        size│       cost│  fee│ rlzd p&l│    price│
    ├──  2023-01-09T14:03:00Z│ TRXBUSD│ BUSD│  3640997.63│  199983.59│ 0.00│     0.00│     0.05│
    ├──  2023-01-09T14:04:00Z│ XRPBUSD│ BUSD│   571265.35│  200191.40│ 0.00│     0.00│     0.35│
    ├──  2023-01-09T14:05:00Z│ BNBBUSD│ BUSD│      719.66│  199869.57│ 0.00│     0.00│   277.73│
    ├──  2023-01-09T14:07:00Z│ BNBBUSD│ BUSD│     -719.66│ -199613.72│ 0.00│  -255.85│   277.37│
    ├──  2023-01-09T14:07:00Z│ XRPBUSD│ BUSD│  -571265.35│ -199294.55│ 0.00│  -896.85│     0.35│
    ├──  2023-01-09T14:07:00Z│ TRXBUSD│ BUSD│ -3640997.63│ -199361.09│ 0.00│  -622.49│     0.05│
    ├──  2023-01-09T14:08:00Z│ ETHBUSD│ BUSD│     -151.51│ -199576.28│ 0.00│     0.00│  1317.25│
    ├──  2023-01-09T14:08:00Z│ BTCBUSD│ BUSD│      -11.59│ -199515.11│ 0.00│     0.00│ 17214.42│
    ├──  2023-01-09T14:12:00Z│ ETHBUSD│ BUSD│      151.51│  200113.21│ 0.00│  -536.92│  1320.79│
    ├──  2023-01-09T14:12:00Z│ BTCBUSD│ BUSD│       11.59│  199726.10│ 0.00│  -210.99│ 17232.62│
    ├──  2023-01-09T14:13:00Z│ BNBBUSD│ BUSD│      717.63│  199449.32│ 0.00│     0.00│   277.93│
    ├──  2023-01-09T14:13:00Z│ XRPBUSD│ BUSD│   569823.99│  199629.30│ 0.00│     0.00│     0.35│
    ├──  2023-01-09T14:19:00Z│ TRXBUSD│ BUSD│  3637915.47│  199814.30│ 0.00│     0.00│     0.05│
    ├──  2023-01-09T14:37:00Z│ TRXBUSD│ BUSD│ -3637915.47│ -199519.71│ 0.00│  -294.59│     0.05│
    ├──  2023-01-09T14:38:00Z│ XRPBUSD│ BUSD│  -569823.99│ -199931.24│ 0.00│   301.94│     0.35│
    ├──  2023-01-09T14:39:00Z│ BNBBUSD│ BUSD│     -717.63│ -200055.24│ 0.00│   605.92│   278.77│
    ├──  2023-01-09T14:41:00Z│ XRPBUSD│ BUSD│   567419.08│  199694.74│ 0.00│     0.00│     0.35│
    ├──  2023-01-09T14:42:00Z│ BNBBUSD│ BUSD│      714.96│  199708.30│ 0.00│     0.00│   279.33│
    ├──  2023-01-09T14:42:00Z│ TRXBUSD│ BUSD│  3630666.41│  199561.38│ 0.00│     0.00│     0.05│
    └──  2023-01-09T14:43:00Z│ ETHBUSD│ BUSD│      150.81│  199751.23│ 0.00│     0.00│  1324.52│ 
// end::fullsummary[]        
    """
