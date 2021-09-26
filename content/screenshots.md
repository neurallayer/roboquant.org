title=Screenshots
date=2021-09-21
type=page
status=published
heading=We can chart our future clearly<br> only when we know the path<br> that led to the present
~~

# Screenshots
When working in a Jupyter Notebook, roboquant comes with several types of charts that help to understand how the strategy is behaving. This pages contains some samples of those charts.

You can try out these charts yourself, since they all come from the visualization notebook.


## Walk forward
Plot a metric during a walk-forward test to provide insight into the performance of the strategy during different timeframes. Here we plot the total account value (= cash + positions) and that provides insights how a strategy performs during different time periods. Clearly we can see that the strategy wasn't profitable during every time frame.

Like most charts in roboquant, you can zoom into areas of interest. You can do so with the slider at the bottom of the chart or use the zoom option from the toolbox at the top right.

<img src="/img/screenshots/walkforward.png" alt="screenshot"/>

## Random periods
An even more extensive back-test is to run the experiment over randomly sampled periods. In this example we selected the period to be 250 trading days. We then plot the results of all samples and this provide a good view of the maximum profit or loss of the strategy during for a certain duration.
<img src="/img/screenshots/randomsamples.png" alt="screenshot" />

## Trades
Trades provide an overview of the trades for all the assets made during a certain time period. 
It can plot different aspects of a trade like the fee or voulme. But one of the most usefull ones is the realized profit & loss. 

<img src="/img/screenshots/trades.png" alt="screenshot" />

## Candlestick
You can see when certain trades were executed in relation to the price of that asset at the same time. 
Especially for strategies based soley on price actions this helps to validate if trades occured when expected.
 
<img src="/img/screenshots/prices.png" alt="screenshot"/>

## Asset allocation
See the asset allocation within the portfolio. If assets are denoted in different currencies, this will converted to the base currency of the account.

<img src="/img/screenshots/assets.png" alt="screenshot" />

## Calendar
Plot a metric for each day of the year to see how it performeed over time. 
This presents a nice overview of good and bad performing days during the bask test. 

The slider at the top of the chart allows to filter the range of values that is of interest.

<img src="/img/screenshots/calendar.png" alt="screenshot" />

## Box plot
Box plot aggregates a metric over a time interval so the different values for certain quantiles can be easily spotted. 
When we plot the change in account value as in the chart below, it provide quick insights into max drawdowns and volatility of our strategy.

<img src="/img/screenshots/box.png" alt="screenshot" />
