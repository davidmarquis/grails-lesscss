# LESS plug-in for Grails

This plug-in adds support for LESS CSS pre-processor to Grails.

It has the following features:

### When in development mode:
 * Automatic in-browser parsing of .less files as CSS when in development.
 * Automatic reload of changes to .less files in development.

### When deploying:
 * Statically compiles .less files into .css and bundles the generated CSS in the final WAR.

Documentation on LESS: [lesscss.org](http://lesscss.org)


# How to use it?

First install it:

    grails install-plugin {path-to-the-plugin-zip-file}

(The plug-in is not yet in the central Grails repository).

Then, in your GSP files:

    <less:stylesheet name="your_stylesheet" />
    <less:scripts />


## <less:stylesheet> tag

Outputs the correct `<link>` HTML element to include your LESS stylesheet.
The included file's extension is determined at runtime and depends on the environment.

Attributes:

  * `name` (required): The name of the stylesheet file, without the .less extension (the correct extension will be dynamically
  set at runtime depending on the environment.
  * `dir` (optional): the directory under `web-app` where the stylesheet file is located (default: 'css')
  * `plugin` (optional): the name of the plug-in into which the stylesheet is bundled (default: none)
  * `absolute` (optional): Should the 'src' generated be absolute or relative? (default: false)
  * `bundled` (optional): Is the stylesheet part of a bundle. If that is the case, then it is assumed that
  the stylesheet is already included when running the application as a WAR. This is useful fo
  integration with ui-performance plug-in that creates (and references) bundles of multiple CSS files.

When in development, the generated `<link>` will link directly to the .less file.
For other environments, the <link> will directly link to the corresponding static .css file
compiled at WAR generation time.

## <less:scripts> tag

Output the other scripts required for in-browser compilation of .less files and auto-reloading.
The tag will only output something when in development mode.


# Thanks

This plug-in has parts based on ui-performance plug-in (most notably the build hooks for static compilation of .less files).

The LESS engine being used is [less.js](https://github.com/cloudhead/less.js/tree/): the next generation LESS compiler that is being developed in Javascript.
Version: 1.0.40

The Java integration has been taken from Asual's fine [Java LESS engine wrapper](https://github.com/asual/lesscss-engine).
Because it is in Javascript, the new less.js implementation can be run using Mozilla Rhino directly in the JVM.

# Enjoy responsibly!
