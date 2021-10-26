<h2>Manual</h2>
<ul class="nav flex-column">
<#list docs as doc>
     <li class="nav-item">
        <a href="/${doc.uri}" class="nav-link">${doc.title}</a>
     </li>
</#list>
</ul>
