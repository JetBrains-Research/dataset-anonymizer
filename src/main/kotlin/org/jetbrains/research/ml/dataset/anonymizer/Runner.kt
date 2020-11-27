package org.jetbrains.research.ml.dataset.anonymizer

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.ApplicationStarter
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.xenomachina.argparser.ArgParser
import org.jetbrains.research.ml.dataset.anonymizer.java.JavaAnonymizer
import org.jetbrains.research.ml.dataset.anonymizer.kotlin.KotlinAnonymizer
import org.jetbrains.research.ml.dataset.anonymizer.python.PythonAnonymizer
import java.nio.file.Paths
import kotlin.system.exitProcess

class Runner : ApplicationStarter {

    private lateinit var inputDir: String

    private var project: Project? = null
    private val logger = Logger.getInstance(this::class.java)

    override fun getCommandName(): String = "anonymizer"

    class AnonymizerRunnerArgs(parser: ArgParser) {
        val input by parser.storing(
            "-i",
            "--path",
            help = "Input directory with data in language folders"
        )
    }

    override fun main(args: Array<out String>) {
        try {
            ArgParser(args.drop(1).toTypedArray()).parseInto(::AnonymizerRunnerArgs).run {
                inputDir = Paths.get(input).toString()
            }
            project = ProjectUtil.openOrImport(getTmpProjectDir(), null, true)
            project?.let { it ->
                val anonymizers = listOf(PythonAnonymizer(it), JavaAnonymizer(it), KotlinAnonymizer(it))
                anonymizers.forEach { anonymizer ->
                    anonymizer.anonymizeLanguageFolder(inputDir)
                }
            }
        } catch (ex: Exception) {
            logger.error(ex)
        } finally {
            exitProcess(0)
        }
    }
}
