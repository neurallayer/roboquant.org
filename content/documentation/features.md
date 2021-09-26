title=features
date=2021-09-21
type=page
status=published
heading=testing leads to failure,<br> and failure leads to understanding
~~

# Features

## Fast
Nowadays, there is so much more data available than ever before. It would be a lost opportunity not being able to leverage this data to make smarter and better strategies. Roboquant can work seamlessly with these large data-sets:

- Fully utilize modern hardware, like big multi-core machines
- Highly tuned back-test engine so the engine itself will not become a bottleneck 
- Run multiple back-test in parallel
- Very fast CSV parsing, allowing to process directories full with CSV files in just seconds
- Support for file formats like Avro to further reduce parsing overhead and easily share data sets 
- Support for lazy loading of data, so memory doesn't become a constraint
- Several very fast algorithms out of the box 

## Flexible
Be able to trade at different markets at different parts in the world. This means:

- Support for trading assets in different currencies at the same time. Roboquant comes even out of the box with currency converters
- Correctly handle data from different time-zones
- Trade traditional asset classes like stocks, options and forex as well as cryptocurrencies
- Support for simple trading strategies and advanced machine learning based approaches
- Support for simple and advanced order types

It is also easy to extend roboquant with:

- New brokers and exchanges
- New type of data feeds ranging from price feeds and fundamental data to unstructured data like images
- Add new advanced order types

## Easy
Developing trading algorithms requires a lot of different skills. You need to know of course a lot about trading itself, but also programming and even a bit of statistics comes in handy. Roboquant tries to cater for both experienced and less experienced user.

### Interactive Development
Roboquant can be run inside a Jupyter notebook which provides a very easy entry for people who might find a full-blown development environment too much hassle. A lot of effort went into designing an API that is very easy to use from a
notebook. 

- Graceful introduction of complexity. In other words: simple things should be simple, complex things should be possible
- Many convenience methods to get results with minimal amount of coding
- Sensible defaults for many arguments, so you don't have to specify them all the time

Once you code base grow too large for a notebook, the same code can be used in a traditional IDE.

### Order management
One of the hardest (and most overlooked aspect) of algorithmic trading is order management, especially in a live trading environment. There are so many things that potentially can go wrong and will cost you real money. It is a real endeavor 
to translate signals into actual orders and manage these outstanding orders. 

Because of this, roboquant separates signals from the actual orders. This allows traders to focus on the generation of 
signals and just pick the order management that fits the risk profile and trading style. And if full control over portfolio- and order-management is required, it is still available. 

### Batteries included
- Over 200 technical indicators that are very easy to use in your own strategies
- Powerful charts when running back tests in a notebook
- Support for many different back-testing approaches
- Pluggable metrics you can use to see how the strategy is performing
- An advanced Simulated broker for back-tests that can handle both basic and advanced order types
- Integration with 3rd party brokers and data providers
 
## Free
In order to build the greatest environment for algo-trading possible, it is important to make sure this environment is free for anyone use, contribute and improve on. 

That is why roboquant is developed under the permissive Apache 2.0 license and welcomes people to contribute. 

But it doesn't stop at the software. For real progress also data has to become freely available so performance of various strategies can be more easily compared. For inspiration look at what quality training data did for deep learning networks and the progress in recent years.
 


