# LESS plug-in for Grails

This plug-in adds support for LESS CSS pre-processor to Grails. Contrarily to some [previous attempt](http://johnnywey.wordpress.com/2010/03/02/grails-less-css-plugin/)
at integrating LESS with Grails, this plug-in does not need a full JRuby stack to work. This is made possible due to the ongoing
'less.js' full javascript rewrite of LESS.

It has the following features:

### When in development mode:
 * Automatic in-browser parsing of .less files as CSS when in development.
 * Automatic reload of changes to .less files in development.

### When deploying:
 * Statically compiles .less files into .css and bundles the generated CSS in the final WAR.
 * Because the generated CSS files are referenced directly, there is absolutely no performance hit in production.

Documentation on LESS syntax and features: [lesscss.org](http://lesscss.org)


# How to use it?

First install it:

    grails install-plugin lesscss

Then, in your GSP files:

    <html>
    <head>
        ...
        <less:stylesheet name="your_stylesheet" />
        <less:scripts />
    </head>


## <less:stylesheet> tag

Outputs the correct `<link>` HTML element to include your LESS stylesheet.

The included file's extension is determined at runtime and depends on the environment.
When in development, the generated `<link>` will link directly to the .less file.
For other environments, the `<link>` element will directly link to the corresponding static .css file
compiled at WAR generation time.

Attributes:

  * `name` (required): The name of the stylesheet file, without the .less extension (the correct extension will be dynamically
  set at runtime depending on the environment).
  * `dir` (optional): the directory under `web-app` where the stylesheet file is located (default: 'css')
  * `plugin` (optional): the name of the plug-in into which the stylesheet is bundled (default: none)
  * `absolute` (optional): Should the 'src' generated be absolute or relative? (default: false)
  * `bundled` (optional): Is the stylesheet part of a bundle? This is useful for integration with ui-performance
  plug-in that bundles multiple CSS files in one file in production. If the stylesheet you're referencing is part of one such bundle,
  then it does not need to be referenced by this tag when running in a WAR. In this particular case, the tag will not output anything.

## <less:scripts> tag

Outputs the JS scripts required for in-browser compilation of .less files and auto-reloading.
The tag will only output something when not running in a WAR (such as in development).

Attributes:

  * `watch` (optional): Set to true if you want less.js to watch your local changes in development mode. It will
  automatically reload and re-compile your LESS code when changes are made. No need to refresh the page in the browser.
  A word of caution: This is known to cause browser storage capacity problems if you keep the page open for too long.
  Defaults to `true`.

# Play nicely with ui-performance

In order for the plug-in to collaborate with ui-performance, the following elements must be taken
into consideration:

 * Since ui-performance minifies and bundles CSS files (and not LESS files), it must run after the lesscss plug-in has
 compiled CSS file during the WAR generation process.
 * In order for the bundles to work correctly in the different environments, you must define a different
 bundle configuration in 'development' environment: the LESS-managed stylesheet must *not* be configured in
 development, because it would result in continuous 404 errors (the .css file being non existent in dev).
 I suggest having two set of configurations for bundles: one for local development and another for the other
 environment (which will contain references to LESS-generated CSS stylesheets).
 * The Mozilla Rhino dependency that this plug-in declares clashes with the Rhino distribution that comes
 with ui-performance (from YUI compressor). If you get a weird unexplainable error at minification time when packaging
 your WAR, try excluding the Rhino dependency from the plug-in (be careful to set the right plug-in version):

     In /grails-app/conf/BuildConfig.groovy:

     dependencies {
        plugins {
            build( ":lesscss:$VERSION$" ) {
                excludes "js"
            }
        }
     }

     Where $VERSION$ corresponds to the exact version of the plug-in you're using.

# Thanks

This plug-in has parts based on ui-performance plug-in (most notably the build hooks for static compilation of .less files).

The LESS engine being used is [less.js](https://github.com/cloudhead/less.js/tree/): the next generation LESS compiler that is being developed in Javascript.

The Java integration has been taken from Asual's fine [Java LESS engine wrapper](https://github.com/asual/lesscss-engine).
Because it is in Javascript, the new less.js implementation can be run using Mozilla Rhino directly in the JVM.

Other contributors:

  * Maxime Lavoie
  * Jorge Silveira

# Enjoy responsibly!
