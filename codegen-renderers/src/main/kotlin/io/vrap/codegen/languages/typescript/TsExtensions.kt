package io.vrap.codegen.languages.typescript

import io.vrap.codegen.languages.java.extensions.resource
import io.vrap.codegen.languages.java.extensions.toRequestName
import io.vrap.codegen.languages.php.extensions.toResourceName
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.raml.model.resources.Method
import io.vrap.rmf.raml.model.resources.Resource
import java.nio.file.Path
import java.nio.file.Paths


fun Method.tsRequestName():String = "${this.toRequestName()}Request"
fun Method.tsRequestModuleName(clientPackageName: String):String = "$clientPackageName.${this.resource().resourcePathName}.${this.toRequestName()}Request"

fun VrapObjectType.tsModuleName() : String = "${this.`package`.replace(".","/")}.ts"

fun Method.tsMediaType(): String{
    return if(this.bodies.isNullOrEmpty() || this.bodies[0].contentMediaType.type().isNullOrEmpty())
        ""
    else
        "'Content-Type': '${this.headers}'"
}

fun String.tsRemoveRegexp():String {
    if(this.startsWith("/")){
        val index = this.indexOf("\\")
        return this.substring(1,index)
    }
    if(this.contains(".")){
        val index = this.indexOf(".")
        return this.substring(index+1,this.length)
    }
    return this
}

fun Resource.tsRequestModuleName(clientPackageName: String):String = "$clientPackageName.${this.resourcePathName}.${this.toResourceName()}RequestBuilder"

fun relativizePaths(currentModule: String, targetModule: String): String {
    val currentRelative: Path = Paths.get(currentModule.replace(".", "/"))
    val targetRelative: Path = Paths.get(targetModule.replace(".", "/"))
    return "./" + currentRelative.relativize(targetRelative).toString().replaceFirst("../", "")
}