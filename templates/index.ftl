<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column h-100">
        <main class="flex-shrink-0">
            <#include "menu.ftl">
            <header class="masthead bg-primary text-white text-center">
                <div class="container">
                    <div class="row gx-2">
                        <div class="col-lg-4 mb-2 mb-lg-0">
                            <img class="mb-0 masthead-avatar" src="/img/avatar.png" alt="${config.site} avatar">
                        </div>
                        <div class="col-lg-8">
                            <div class="center">
                            <h1 class="fw-light mt-1"><span>r</span>obo<span>q</span>uant</h1>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <section class="pt-3" id="intro">
                <div class="container px-3 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">What is roboquant</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                <p><em>Roboquant</em> is an open-source algorithmic trading platform written in Kotlin. It is flexible, user-friendly and completely free to use. It is designed for anyone serious about algo-trading. So whether you are a beginning retail trader or an established trading firm,
                                    <em>roboquant</em> can help you to quickly develop robust and fully automated trading strategies.
                                </p>

                                <p>But perhaps most important of all, it is blazingly fast. Roboquant is orders of magnitude faster than most other algo-trading platforms.</p>
                                <div class="imageblock">
                                    <div class="content">
                                        <img src="/img/performance_overview.png" alt="screenshot" />
                                    </div>
                                </div>
                                <p>With historic data sets becoming more widely available and growing in size, it is important that a strategy can still be quickly developed, back-tested and optimized. If this cycle takes too long, it is nearly impossible to create high performing and robust strategies.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="py-3" id="sample2">
                <div class="container px-3 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">Simple to get started</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                A lot of effort and attention went into making sure <em>roboquant</em> is easy to use, especially for less experienced developers.
                                The following example shows all the steps required to run a full back test in a Jupyter notebook.
                                <br></br>
                                <div class="imageblock">
                                    <div class="content">
                                        <img src="/img/code.gif" alt="screenshot" />
                                    </div>
                                </div>
                                As you can see, it is not difficult to get started. So if you are a beginning developer, give <em>roboquant</em> notebooks a try. And if you are an
                                experienced developer or want to develop complex strategies, try out the Kotlin libraries.
                                <form action="/tutorial/install.html">
                                    <button class="btn btn-outline-dark float-end" id="install_software" type="submit">Get the software</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="pb-3" id="features">
                <div class="container px-3 my-3">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">Features</h2></div>
                        <div class="col-lg-8">
                            <div class="row gx-5 row-cols-1 row-cols-md-2">
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-1"><span>Fast</span></div>
                                    <ul class="feature-list">
                                        <li>Process millions of candles per second</li>
                                        <li>500x faster than Zipline or backtrader</li>
                                        <li>Run back tests in parallel</li>
                                        <li>Sub millisecond trading</li>
                                    </ul>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-1"><span>Flexible</span></div>
                                    <ul class="feature-list">
                                        <li>Stocks, crypto, options, forex, and more</li>
                                        <li>Multi-currency & multi-region</li>
                                        <li>Advanced order types</li>
                                        <li>Highly configurable simulation engine</li>
                                    </ul>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-1"><span>Friendly</span></div>
                                    <ul class="feature-list">
                                        <li>Develop with Jupyter Notebooks</li>
                                        <li>Dedicated algo-trading charts</li>
                                        <li>150+ technical indicators included</li>
                                        <li>Easy to use APIs</li>
                                    </ul>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-1"><span>Free</span></div>
                                    <ul class="feature-list">
                                        <li>All source code on GitHub</li>
                                        <li>Permissive Apache 2.0 license</li>
                                        <li>Code contributions are appreciated</li>
                                        <li>Welcoming community</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="pb-3" id="integration">
                <div class="container px-3 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">3rd Party Providers</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                <#include "integration.ftl">
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </main>
       <#include "footer.ftl">
    </body>
</html>
