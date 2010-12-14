class LesscssGrailsPlugin {
    // the plugin version
    def version = "0.9.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    def loadAfter = ['ui-performance']

    def author = "David Marquis"
    def authorEmail = "davidmarquis@me.com"
    def title = "Less CSS"
    def description = '''\\
Integration of the Less CSS framework into Grails. Less (lesscss.org) is what CSS should have always
been. The plug-in allows for automatic compilation of .less files into .css files, as well as
tools for easier development (such as auto-reload of CSS after changes in development environment).
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-lesscss"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
