title=design
date=2021-09-21
type=page
status=published
heading=testing leads to failure,<br> and failure leads to understanding
~~~~~~

# Design decisions
This document provides an overview of the high level design choices, and the rational why they were made. It is provided mainly as background information. And it can also help to decide if roboquant is the right solution for you.


## Why develop a new framework
Before we start developing roboquant, we of course looked at existing frameworks to see if these could fit the bill. However for our use-cases none of them checked the boxes that we required:

* Trade assets in multiple currencies at the same time
* Support many asset classes including cryptocurrencies 
* Solid live trading, including support for advanced order types to reduce risk
* Deal with large volumes of data and run extensive back-tests
* Support for trading in multiple markets in different regions at the same time

## Why use Kotlin
There are many computer languages available to choose from. After having developed several prototypes in different languages, 
we decided to go with Kotlin. Some main benefits being:

* Easy to learn and use with excellent tooling support
* It is very concise with almost no boilerplate code required
* You can use it both within Jupyter Notebooks and traditional IDE's
* Fast enough to even handle the largest data sets during back testing
* Access to the huge Java ecosystem with a library available for almost any task at hand
* Excellent for building robust software, a necessity when developing complex trading strategies and your own money is at risk

#### Why not Python
One often asked question is "Why didn't you use Python instead?". The truth to be said, we would have loved to be able 
to use Python. However, during the prototype phase we discovered that the performance was lacking too much for our 
use-cases. This was including using libraries like Numpy and Pandas and even some Cython to speedup processing 
where possible.

The main reason for this lackluster performance is not only the language itself. But also the fact that using all
the cores found on modern CPU's is difficult to do with Python without a lot of overhead.

The good news is that if you really want to stick with Python, there are plenty of alternatives available. For example, 
you could have a look at backtrader, pyalgotrade or zipline.

## Interactive and traditional development
From the start we developed *roboquant* to be used either as a library included in your application or interactively from a Jupyter notebook. The various API's have been designed so that they support both use-cases.

Where many Java based applications require a lot of ceremony to implement even the simplest piece of functionality, *roboquant* it designed to remove most of that. Especially in a Jupyter notebook environment, it is very easy to get started. Because of this the API exposed by roboquant follows certain rules:

- Minimize need for imports, for example don't expose third party types if it can be avoided
- Use sensible defaults for method parameters where possible  
- Use informative toString() implementations and explanatory exception messages
- Provide overloaded convenience methods if it makes life easier for the user of the API
- Use a flat package structure, so any import is simple to remember
- Leverage type inference where possible so no additional type info needs to be provided


## Modules
In order to keep the project manageable and also reduce the download sizes, roboquant is divided into a number
of distinct modules:

- roboquant-core: all the functionality required for back testing a strategy
- roboquant-extra: adds support for data feeds and 3rd party brokers
- roboquant-crypto: adds support for cryptocurrency feeds and exchanges
- roboquant-ml: adds support for machine learning strategies. This is a huge module because the dependent packages tend to be very large. Right now, this module is not published yet.

This approach allows people with limited hardware to only load the modules required. 

## Time handling
When testing strategies that trade simultaneously in multiple time-zones, like stocks in New York and London, it is key to have proper time management. Otherwise, you run the risk that your strategy performs very well but only because it can peep into the future due to faulty handling of time-zones.

Because of that, roboquant internally only uses the Java Instant type to represent a moment in time. So all time events can be easily compared without having to consider time zones. 

When importing data, there is functionality to convert the data from timezone specific value towards the Instant type. When displaying time related information, it can be converted back the other way around if desired.

But the fact remains that all internal logic relies only on the Instant type in order to prevent timezone mistakes from happening.

## Performance
With data sets growing, it is important that a strategy can still be quickly evaluated and tuned when required. If this develop-test cycle becomes too slow, it is nearly impossible to create high performing strategies. 

- Roboquant isn't setup to be a ledger and does not require the same accuracy when it comes to trading data. The native Double type provides enough precision for any trading purpose and there is no need to use (the much slower and memory hungry) BigDecimal type.
  
- The data Feed API supports data that is kept in-memory as well data that is stored on disk and only accessed when required. This allows for running back test where the test data doesn't fit in memory or running back-tests on machines with limited memory. In fact roboquant can be used on a JVM with only 512 MB of heap allocated.
  
- Many of the builtin features support multi-core machines to speedup processing. For example the CSV parser can parse CSV files in parallel so even directories with thousands of CSV files can be parsed in seconds.
  
- Avoid (auto)boxing, so the JVM can access these variables directly. This means where possible use native types and not the wrapper one. See [background](https://docs.oracle.com/javase/1.5.0/docs/guide/language/autoboxing.html)
  
- Overall the latency is kept very low when processing a new event and generating a signal and order. So it is feasible to create fast, low latency (sub-second) trading strategies.
  
## Reliability
Even more important than performance, is reliability when it comes to trading software. 

- All components can log information that is then available for audit and tracing
- Type and null checks where possible to leverage the compiler to identify possible mistakes
- Extensive error logging to alert possible issues, including data quality
- Assert/requires to validate input parameters
- Good unit test suite to cover much of the code base
- Proven third party libraries

