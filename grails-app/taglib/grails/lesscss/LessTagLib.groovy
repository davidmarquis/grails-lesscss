package grails.lesscss

import grails.util.Environment
import groovy.xml.MarkupBuilder
import com.github.grails.lesscss.Constants
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.commons.GrailsApplication

class LessTagLib {

    static namespace = "less"

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    /**
     * If in development environment, this tag will generate links to .less stylesheets directly.
     * Otherwise, it will generate links to statically compiled CSS files of the same name. These
     * files are generated at WAR-packaging time.
     *
     * Attributes:
     *
     * name: name of stylesheet to include (without the file extension, which will be filled depending
     *        on the context
     * dir: name of the directory (relative to /web-app) that contains the .less file. Defaults to 'css'.
     * plugin: name of the plugin into which the .less file resides (defaults to none, meaning current app)
     * contextPath: the context path to use (relative to the application context path). Defaults to "" if plugin 
     *                        attribute is not defined.
     * absolute: whether to generate a fully absolute href URL for the stylesheet.
     * bundled: (true/false) whether the stylesheet is part of a bundle or not. When this is the case,
     *                        no <link> element will be output when running in production. We assume that
     *                        the final stylesheet is part of a bundle included by another plug-in (such
     *                        as ui-performance)
     *
     */
    def stylesheet = { attrs, body ->

        if (shouldSkipLink(attrs)) {
            return
        }

        String name = attrs.remove('name')
            if (!name) {
              throwTagError("Tag [less] is missing required attribute [name]")
        }

        String rel, fileType, link, html
        String dir = attrs.remove('dir') ?: 'css';
        if (isUsingAutoCompile()) {
            // reference .less files directly (less.js will automatically compile into CSS)
            rel = 'stylesheet/less'
            fileType = '.less'
        } else {
            rel = 'stylesheet'
            fileType = '.css'
        }

        link = generateRelativePath(dir, name, fileType, attrs.remove('plugin'), attrs.remove('contextPath'), attrs.remove('absolute'))

        def mkp = new MarkupBuilder(out)
        def params = [rel: rel, type: 'text/css', href: link]
        params.putAll attrs
        mkp.link(params)
    }

    def scripts = { attrs, body ->

        if (isUsingAutoCompile()) {
            String src = generateRelativePath('js', Constants.LESS_SCRIPT_NAME, '.js', "lesscss", "", false)

            out << "<script type=\"text/javascript\" src=\"${src}\"></script>"

            if (isUsingAutoReload(attrs)) {
              out << '''<script type="text/javascript">
                          less.env = "development";
                          less.watch();
                        </script>'''
            }
        }
    }

    /**
     * When 'bundled' attribute is specified, we will not output links when running in a WAR.
     */
    private boolean shouldSkipLink(def attrs) {
        if ("true" == attrs.remove("bundled")) {
            return grailsApplication.isWarDeployed()
        }
        return false
    }

    private boolean isUsingAutoCompile() {
        return !grailsApplication.isWarDeployed()
    }

    private boolean isUsingAutoReload(attrs) {
        def watch = attrs.watch
        return (watch == null || watch == "true")
    }

    private String generateExtraAttributes(attrs) {

        def extra = ""
        attrs.each { key, value ->
            extra << " $key=\"$value\""
        }
        return extra
    }

    private String generateRelativePath(dir, name, extension, plugin, contextPath, absolute) {
        if ('true' == absolute) {
            return name
        }

        StringBuilder path = new StringBuilder()
        path << g.resource(plugin:plugin ?: null, contextPath: contextPath ?: '', dir: dir, file: name)
        if (extension) {
            path << extension
        }

        return path.toString().replaceAll('//', '/')
    }
}
