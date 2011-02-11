package com.github.grails.lesscss

import com.asual.lesscss.LessEngine


class LessEngineHelper {

    public void compileLessCss(stagingDir, baseDir) {
        println "Compiling LESS files from dir ${stagingDir}"

        LessEngine engine = new LessEngine()
        def lessFiles = []
        
        stagingDir.eachFileRecurse { file ->

            if (file.directory) {
              return
            }

            if (file.name.toLowerCase().endsWith(Constants.LESS_EXTENSION)) {
                def input = file
                def output = getOutputFile(file)
                
                if (!lessFiles.contains(input)) {
                    println "Compiling LESS file [${input}] into [${output}]"
                    engine.compile input, output
                    filterFileContent output
                    lessFiles.add(input)
                }
            }
        }
        removeLessFiles(lessFiles)
    }

    private File getOutputFile(File input) {
        def inputStr = input as String
        new File(inputStr.substring(0, inputStr.size() - Constants.LESS_EXTENSION.length()) + ".css")
    }

    private void filterFileContent(file) {
        String text = file.text

        // this is required because of an incompatibility issue with YUI Compressor's Rhino distribution
        // (from ui-performance) plugin, which inserts "\n" sequences in final CSS output (!)
        text = text.replaceAll(/\\n/, "")

        file.write(text)
    }
    
    private void removeLessFiles(lessFiles) {
      lessFiles.each{ file -> file.delete() }
    }
}