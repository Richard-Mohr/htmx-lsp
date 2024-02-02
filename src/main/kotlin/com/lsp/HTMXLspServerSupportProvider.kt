package com.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.PluginPathManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor

class HTMXLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (file.extension == "foo") {
            serverStarter.ensureServerStarted(HTMXLspServerDescriptor(project))
        } else {
            println("File type is %s".format(file.extension))
        }
    }
}

private class HTMXLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Foo") {
    override fun isSupportedFile(file: VirtualFile) = file.extension == "foo"
    override fun createCommandLine(): GeneralCommandLine {
        val lsp = PluginPathManager.getPluginResource(javaClass, "language-server/main.go")

        val command = GeneralCommandLine("go")
        command.withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        command.withCharset(Charsets.UTF_8)
        command.addParameters("run", lsp?.path)
        return command
    }

}

