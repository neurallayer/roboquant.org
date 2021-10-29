<#assign sections = ["install", "concepts",  "roboquant", "feed", "strategy", "policy", "metrics", "integration"]>
<nav id="sidebar">
    <h3>Documentation</h3>
    <#list sections as section>
        <details>
            <summary>${section}</summary>
            <ul class="nav flex-column">
                <#list docs as doc>
                     <#if doc.uri?starts_with("documentation/" + section)>
                         <li class="nav-item">
                            <a href="/${doc.uri}" class="nav-link">${doc.title}</a>
                         </li>
                     </#if>
                </#list>
            </ul>
        </details>
    </#list>
</nav>