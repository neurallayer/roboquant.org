# roboquant website

![Automated Deploy](https://github.com/neurallayer/roboquant.org/actions/workflows/gh-pages.yml/badge.svg)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/neurallayer/roboquant.org)
![APL v2](https://img.shields.io/badge/license-Apache%202-blue.svg)
![GitHub issues by-label](https://img.shields.io/github/issues/neurallayer/roboquant.org/bug?color=red&label=bugs)
![GitHub issues by-label](https://img.shields.io/github/issues/neurallayer/roboquant.org/enhancement?color=yellow&label=enhancements)
![GitHub last commit](https://img.shields.io/github/last-commit/neurallayer/roboquant.org)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/neurallayer/roboquant.org)

This repo contains the content for the roboquant.org website. It uses JBake as the generator for the static website and AsciiDoc for most of the documentation. All the Kotlin code samples used in the documentation are in the samples directory and can be compiled to make sure they are correct and still up to date.

Check the roboquant site at [roboquant.org](https://roboquant.org)

## Build
To generate the website (output will be in the `./build directory)`, run:

```shell
mvn jbake:generate
```

If you also want to validate if the included code snippets (found in the `./samples` directory) still compile, you can run:
```shell
mvn compile
```

**📌 NOTE**\
If you are on Apple Silicon (the M1 processor), the default JBake installation won’t work. But the JBake Maven plugin used in this project has an upgraded version of the OrientDB library that should work (but not very well tested).

To interactively develop, you can run:

```shell
mvn jbake:inline
```

## Deploy
The https://roboquant.org site is actually a GitHub Pages website. Using the action as defined in `.github/workflows/gh-pages.yml`, the static content for the website is automatically generated and deployed after each new push on the main branch. So there is no need for a manual step to update the website after a change has been committed to this repository.

## Other
If you want to fork this for your own website, please be aware that some templates still have hard coded content in there that you want to replace.
