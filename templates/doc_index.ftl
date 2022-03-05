<#assign sections = ["introduction",  "installation", "roboquant", "feed", "strategy", "policy", "broker", "metrics", "jupyter", "multimarkets", "integration"]>
<nav id="sidebar">
    <h3>Documentation</h3>
    <#list sections as section>
        <#if content.uri?starts_with("documentation/" + section)>
            <#assign state="open" />
        <#else>
            <#assign state="" />
        </#if>
        <details ${state}>
            <summary>${section}</summary>
            <ul class="nav flex-column">
                <#list docs as doc>
                     <#if doc.uri?starts_with("documentation/" + section)>
                 <li class="nav-item"><a href="/${doc.uri}" class="nav-link">${doc.title}</a></li>
                     </#if>
                </#list>
            </ul>
        </details>
    </#list>
</nav>