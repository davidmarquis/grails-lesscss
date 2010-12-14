package com.github.grails.lesscss

import com.asual.lesscss.LessEngine


class LessEngineHelper {

    public void compileLessCss(stagingDir, baseDir) {
        println "Compiling LESS files from dir ${stagingDir} / ${baseDir}"

        LessEngine engine = new LessEngine()

        stagingDir.eachFileRecurse { file ->
			if (file.directory) {
				return
			}

            if (file.name.toLowerCase().endsWith(Constants.LESS_EXTENSION)) {

                def input = file
                def output = getOutputFile(file)

                println "Compiling LESS file [${input}] into [${output}]"

                engine.compile input, output

                input.delete()
            }
		}

    }

    private File getOutputFile(File input) {
        def inputStr = input as String
        new File(inputStr.substring(0, inputStr.size() - Constants.LESS_EXTENSION.length()) + ".css")
    }


}
