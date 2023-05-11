<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column">
        <main class="flex-shrink-0">
              <#include "menu.ftl">
              <header class="masthead bg-primary text-white text-center">
                     <div class="container px-5 my-1">
                         <div class="row gx-5">
                            <div class="col-lg-4 mb-2 mb-lg-0">
                            </div>
                             <div class="col-lg-8">
                                 <div class="col h-100">
                                    <h1 class="fw-light mt-1 doc">${content.title}</h1>
                                 </div>
                             </div>
                         </div>
                     </div>
                 </header>

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
