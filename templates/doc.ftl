<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column">
        <main class="flex-shrink-0">
            <#include "menu.ftl">
            <#include "header.ftl">

            <section class="py-2">
                <div class="container px-3 my-1">
                    <div class="row gx-5">
                        <div class="col-lg-4 mb-2 mb-lg-0">
                            <#include "doc_index.ftl">
                         </div>
                        <div class="col-lg-8">
                            <div class="col h-100">
                                ${content.body}
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <#include "footer.ftl">
    </body>
</html>
