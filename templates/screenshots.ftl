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
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-5 mb-lg-0">
                            <img class="mb-2 masthead-avatar" src="/img/avatar.png" alt="${config.site} avatar">
                        </div>
                        <div class="col-lg-8">
                            <h1 class="font-weight-light mt-2">${content.title}</h1>
                            <h4 class="masthead-subheading font-weight-light mt-2">${content.heading}</h4>
                        </div>
                    </div>
                </div>
            </header>

            <#list screenshotitems as screenshot>
            <section class="pt-3" id="intro">
                <div class="container px-5 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">${screenshot.title}</h2></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                ${screenshot.body}
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            </#list>

        </main>
       <#include "footer.ftl">
    </body>
</html>
