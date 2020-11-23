package org.jetbrains.research.ml.dataset.anonymizer

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationStarter
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.SystemExitException
import org.jetbrains.research.ml.dataset.anonymizer.util.Language
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess

private fun File.isLanguageFolder(): Boolean {
    if (!this.isDirectory) {
        return false
    }
    try {
        Language.valueOf(this.name.toUpperCase())
    } catch (e: IllegalArgumentException) {
        return false
    }
    return true
}

private fun getAnonymizerList(rootPath: String): List<Anonymizer> {
    val root = File(rootPath)
    val anonymizers = emptyList<Anonymizer>()
    root.listFiles()?.filter { it.isLanguageFolder() }?.forEach { folder ->
        val language = Language.valueOf(folder.name.toUpperCase())
        println(language)
//        val anonymizer = when(language) {
//            Language.PYTHON -> TODO()
//            Language.JAVA -> TODO()
//        }
    }
    return anonymizers
}

class Runner: ApplicationStarter {

    private lateinit var inputDir: String

    private var project: Project? = null
    private val logger = Logger.getInstance(this::class.java)

    override fun getCommandName(): String = "anonymizer"

    class AnonymizerRunnerArgs(parser: ArgParser) {
        val input by parser.storing(
            "-i", "--path",
            help = "Input directory with data in language folders"
        )
    }

    override fun main(args: Array<out String>) {
        try {
            ArgParser(args.drop(1).toTypedArray()).parseInto(::AnonymizerRunnerArgs).run {
                inputDir = Paths.get(input).toString()
            }
            project = ProjectUtil.openOrImport(getTmpProjectDir(), null, true)
            getAnonymizerList(inputDir)

        } catch (ex: SystemExitException) {
            logger.error(ex)
        } catch (ex: Exception) {
            logger.error(ex)
        } finally {
            exitProcess(0)
        }
    }
}
