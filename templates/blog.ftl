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
                        <div class="col-lg-4 mb-2 mb-lg-0">
                            <img class="mb-2 masthead-avatar" src="/img/avatar.png" alt="${config.site} avatar">
                        </div>
                        <div class="col-lg-8">
                            <h1 class="fw-light mt-2">${content.title}</h1>
                            <h4 class="masthead-subheading fw-light mt-2">${content.heading}</h4>
                        </div>
                    </div>
                </div>
            </header>

            <section class="pt-3" id="intro">
                <div class="container px-3 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-1 mb-lg-0"></div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                ${content.body}
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <#list items?sort_by("uri") as item>
                <#if item.uri?starts_with(content.items)>
                <section class="pt-3">
                    <div class="container px-3 my-1">
                        <div class="row gx-5">
                            <div class="col-lg-4 mb-1 mb-lg-0"><h2 id="${item.title}" class="fw-bolder mb-0">${item.title}</h2></div>
                            <div class="col-lg-8">
                                <div class="col h-100">
                                    ${item.intro}
                                    <br/><br/>
                                    <p>
                                    Click <a href="${item.uri}">here</a> to read the full article.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
                </#if>
            </#list>

        </main>
       <#include "footer.ftl">
    </body>
</html>
