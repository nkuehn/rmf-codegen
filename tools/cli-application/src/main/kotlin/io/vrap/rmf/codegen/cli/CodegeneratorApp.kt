@file:JvmName("MainKt")

package io.vrap.rmf.codegen.cli

import io.vrap.rmf.codegen.cli.info.BuildInfo
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val exitCode = CommandLine(RMFCommand())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(*args)
    exitProcess(exitCode)
}

@Command(
        version = [BuildInfo.VERSION],
        description = ["Allows to validate Raml files and generate code from them"],
        subcommands = [GenerateSubcommand::class,VerifySubcommand::class]
        )
class RMFCommand : Runnable {

    @Option(names = ["-v", "--version"], versionHelp = true, description = ["print version information and exit"])
    var versionRequested = false

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display this help message"])
    var usageHelpRequested = false

    override fun run() {
        println("Please invoke a subcommand");
        CommandLine(this).usage(System.err);
    }
}
