<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column">
        <main class="flex-shrink-0">
            <!-- Navigation-->
             <#include "menu.ftl">
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
            <!-- Page content-->
            <section class="py-3">
                <div class="container-sm px-5 my-5">
                     ${content.body}
               </div>
            </section>
        </main>
        <!-- Footer-->
        <#include "footer.ftl">
    </body>
</html>
