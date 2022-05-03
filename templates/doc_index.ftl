 <nav id="sidebar" class="navbar fixed-left sticky-top">

    <ul class="list-unstyled">
    <span class="navbar-brand">Content</span>
    <#list docs as doc>

        <#if content.uri?starts_with("tutorial/")>
            <#if doc.uri?starts_with("tutorial/")>
               <li class="nav-item"><a href="/${doc.uri}" class="nav-link">${doc.title}</a></li>
            </#if>
        </#if>

        <#if content.uri?starts_with("background/")>
            <#if doc.uri?starts_with("background/")>
                 <li class="nav-item"><a href="/${doc.uri}" class="nav-link">${doc.title}</a></li>
            </#if>
        </#if>

    </#list>
    </ul>
</nav>
