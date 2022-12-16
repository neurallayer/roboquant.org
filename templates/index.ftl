<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column h-100">
        <main class="flex-shrink-0">
            <!-- Navigation-->
            <#include "menu.ftl">
            <!-- Header-->
            <header class="masthead bg-primary text-white text-center">
                <div class="container d-flex align-items-center flex-column">
                    <!-- Masthead Banner Image-->
                    <img class="masthead-banner mb-1" src="/img/banner.png" alt="${config.site} banner">
                </div>
            </header>

            <section class="pt-3" id="intro">
                <div class="container px-5 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">What is roboquant</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                <p><em>Roboquant</em> is an open source algorithmic trading platform written in Kotlin. It is very fast, flexible, user-friendly and completely free to use.</p>
                                <p>It is designed for anyone serious about algo-trading. So whether you are a beginning retail trader or an established trading firm,
                                <em>roboquant</em> can help you to quickly develop fully automated trading strategies. No false promises of making lots of
                                 profit without doing the hard work, just a great foundation for building your own strategies.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="py-3" id="sample">
                <div class="container px-5 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">Simple to get started</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                A lot of attention went into making sure <em>roboquant</em> is easy to use, especially for less experienced developers.
                                The following code snippet shows all the key ingredients required to run a back test.
                                <br>
                                <#include "snippet.ftl">
                                <form action="/tutorial/install.html">
                                <button class="btn btn-outline-dark float-end" id="install_software" type="submit">Get the software</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Features section-->
            <section class="pb-3" id="features">
                <div class="container px-5 my-3">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">Main Features</h2></div>
                        <div class="col-lg-8">
                            <div class="row gx-5 row-cols-1 row-cols-md-2">
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-2"><span> Fast </span></div>
                                    <p class="mb-0">Run back tests over large historic data sets in seconds. Run multiple tests at the same time, fully utilizing modern multicore CPU's.</p>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-2"><span> Flexible </span></div>
                                    <p class="mb-0">Trade assets of different currencies, markets, and asset classes at the same time. Support for cryptocurrencies as wel a traditional assets.</p>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-2"><span> Friendly </span></div>
                                    <p class="mb-0">Develop your strategies interactively in Jupyter notebooks. Batteries are included with over 150+ technical indicators and many charts.</p>
                                </div>
                                <div class="col mb-3 h-100">
                                    <div class="feature bg-primary bg-gradient text-white rounded-3 mb-2"><span> Free </span></div>
                                    <p class="mb-0">Available under the permissive Apache 2.0 license with the source code available on GitHub. Contributions are highly appreciated.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="pb-3" id="integration">
                <div class="container px-5 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">3rd Party Integration</h2></div>
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
