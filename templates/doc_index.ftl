<nav id="sidebar" class="navbar fixed-left sticky-top navbar-light">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent2" aria-controls="navbarSupportedContent2" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
    <div class="collapse navbar-collapse show" id="navbarSupportedContent2">
        <ul class="navbar-nav">
        <#list docs as doc>
            <#if doc.uri?starts_with(content.uri?substring(0, 4))>
                <li class="nav-item"><a href="/${doc.uri}" class="nav-link">${doc.title}</a></li>
            </#if>
        </#list>
        </ul>
    </div>
</nav>
