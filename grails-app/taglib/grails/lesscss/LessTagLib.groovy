package grails.lesscss

import grails.util.Environment
import groovy.xml.MarkupBuilder
import com.github.grails.lesscss.Constants
import org.codehaus.groovy.grails.plugins.GrailsPluginManager

class LessTagLib {

    static namespace = "ui"

    GrailsPluginManager pluginManager

    /**
     * If in development environment, this tag will generate links to
     */
    def less = { attrs, body ->


		String name = attrs.remove('name')
		if (!name) {
			throwTagError("Tag [less] is missing required attribute [name]")
		}

        String rel, fileType, link, html
        String dir = attrs.remove('dir') ?: 'css';
        if (isUsingAutoReload()) {
            // reference .less files directly (less.js will automatically compile css)
            rel = 'stylesheet/less'
            fileType = '.less'
        } else {
            rel = 'stylesheet'
		    fileType = '.css'
        }

        link = generateRelativePath(dir, name, fileType, attrs.remove('plugin'), attrs.remove('absolute'))


        def mkp = new MarkupBuilder(out)
        def params = [rel: rel, type: 'text/css', href: link]
        params.putAll attrs
        mkp.link(params)
	}

    def lessScripts = { attrs, body ->

        if (isUsingAutoReload()) {
            String src = generateRelativePath('js', Constants.LESS_SCRIPT_NAME, '.js', "lesscss", false)

            out << "<script type=\"text/javascript\" src=\"${src}\"></script>"
        }

        if (isWatching()) {
            out << '''
                    <script type="text/javascript">
                        less.env = "development";
                        less.watch();
                    </script>
                    '''
        }
    }

    protected boolean isUsingAutoReload() {
        return Environment.isDevelopmentMode()
    }

    protected boolean isWatching() {
        return Environment.isDevelopmentMode()
    }

    protected String generateExtraAttributes(attrs) {

		def extra = ""

		attrs.each { key, value ->
			extra << " $key=\"$value\""
		}

		return extra
	}

    protected String generateRelativePath(dir, name, extension, plugin, absolute) {
		if ('true' == absolute) {
			return name
		}

		StringBuilder path = new StringBuilder()
		path << g.resource(plugin:plugin, dir: dir, file: name)
		if (extension) {
			path << extension
		}

		return path.toString().replaceAll('//', '/')
	}
}
