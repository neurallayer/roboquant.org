<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column h-100">
        <main class="flex-shrink-0">
            <!-- Navigation-->
            <#include "menu.ftl">
            <#include "header.ftl">

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

            <#list docs as item>
                <#if item.uri?starts_with("blog/")>
                <section class="pt-3">
                    <div class="container px-3 my-1">
                        <div class="row gx-5">
                            <div class="col-lg-4 mb-1 mb-lg-0"><h2 class="fw-bolder mb-0">${item.date?date}</h2></div>
                            <div class="col-lg-8">
                                <div class="col h-100">
                                    <h3> ${item.title}</h3>
                                    <p>${item.intro}</p>
                                    <p>Click <a href="/${item.uri}">here</a> to read the full article.</p>
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
