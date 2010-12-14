import grails.util.GrailsUtil

/**
 * In 1.0.x this is called after the staging dir is prepared but before the war is packaged.
 */
eventWarStart = { name ->
	if (name instanceof String || name instanceof GString) {
		compileLessCss name, stagingDir
	}
}

/**
 * In 1.1 this is called after the staging dir is prepared but before the war is packaged.
 */
eventCreateWarStart = { name, stagingDir ->
	compileLessCss name, stagingDir
}

void compileLessCss(name, stagingDir) {
	def classLoader = Thread.currentThread().contextClassLoader
	classLoader.addURL(new File(classesDirPath).toURL())

	def config = new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('Config')).lessCss
	def enabled = config.enabled
	enabled = enabled instanceof Boolean ? enabled : true

	if (!enabled) {
		println "\nLESS CSS not enabled, not processing .less files.\n"
		return
	}

	println "\nLESS CSS: compiling .less files ...\n"

	String className = 'com.github.grails.lesscss.LessEngineHelper'
	def helper = Class.forName(className, true, classLoader).newInstance()
	helper.compileLessCss stagingDir, basedir
}
