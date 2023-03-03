<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<#list published_content as content>
    <url>
        <loc>${config.site_host}/${content.uri}</loc>
        <lastmod>${published_date?string("yyyy-MM-dd")}</lastmod>
    </url>
</#list>
</urlset>
