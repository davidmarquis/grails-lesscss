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

    <ui:less name="your_stylesheet" />
    <ui:lessScripts />


## <ui:less> tag

Output correct <link> element to include your LESS stylesheet. The file extension is determined at runtime
and depends on the environment.

Attributes:

  * name (required): The name of the stylesheet file, without the .less extension (the correct extension will be dynamically
  set at runtime depending on the environment.
  * dir (optional): the directory under `web-app` where the stylesheet file is located (default: 'css')
  * plugin (optional): the name of the plug-in into which the stylesheet is bundled (default: none)
  * absolute (optional): Should the 'src' generated be absolute or relative? (default: false)

When in development, the <link> will link directly to the .less file. For other environments, the <link>
will directly link to the static .css file compiled at WAR generation time.

## <ui:lessScripts> tag

This is required to support in-browser compilation of .less files.
The tag will only output something when in development mode.


# Enjoy!