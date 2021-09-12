# Intro
This repo contains the content for the roboquant.org website. It uses JBake as the generator for the static website.

# Build
Not the default output directory is used, but rather the docs directory. The main reason for this is that the static
website is hosted by GitHub pages and right now only / or /docs can serve as the content root and not other directories.

To generate the website, run:

```shell
jbake -b . docs
```

To develop, you can run: 
```shell
jbake -s . docs
```

# Other
Check the roboquant site at [roboquant.org](https://roboquant.org)

If you want to fork this for your own website, please be aware that the index.ftl template still has some hard coded 
content in there that you need to replace. The other pages use the content.

