<!DOCTYPE html>
<html lang="en">
    <#include "head.ftl">
    <body class="d-flex flex-column">
        <main class="flex-shrink-0">
            <!-- Navigation-->
             <#include "menu.ftl">
            <#include "header.ftl">
            <!-- Page content-->
            <section class="py-3">
                <div class="container-sm px-3 my-3">
                     ${content.body}
               </div>
            </section>
        </main>
        <!-- Footer-->
        <#include "footer.ftl">
    </body>
</html>
