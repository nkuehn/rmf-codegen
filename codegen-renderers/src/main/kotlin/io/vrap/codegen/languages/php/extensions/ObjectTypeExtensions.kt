package io.vrap.codegen.languages.php.extensions

import io.vrap.codegen.languages.ExtensionsBase
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.rmf.codegen.types.*
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property


interface  ObjectTypeExtensions : ExtensionsBase {

    fun ObjectType.getImports(): List<String> {
        return getImports(this.properties)
    }

    fun ObjectType.getImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { it.type }
                //If the subtipes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .filterNotNull()
                .filter { !it.isScalar() }
                .map { vrapTypeProvider.doSwitch(it) }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .sortedBy { it }
                .distinct()
                .toList()

        if(result.contains("")){
            println()
        }

        return result
    }

    fun ObjectType.getPropertyImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { vrapTypeProvider.doSwitch(it.eContainer()) }
                .filter { s -> when(s) {
                        is VrapObjectType -> true
                        is VrapArrayType -> when (s.itemType) {
                            is ObjectType -> true
                            else -> false
                        }
                        else -> false
                    }
                }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .sortedBy { it }
                .distinct()
                .toList()

        if(result.contains("")){
            println()
        }

        return result
    }

    fun ObjectType.getObjectImports(properties: List<Property>): List<String> {
        val vrapType = vrapTypeProvider.doSwitch(this)
        val result =  properties
                .map { it.type }
                //If the subtipes are in the same package they should be imported
                .plus(this.namedSubTypes())
                .plus(this.type)
                .filterNotNull()
                .filter { !it.isScalar() }
                .map { vrapTypeProvider.doSwitch(it) }
                .filter { s -> when(s) {
                        is VrapObjectType -> true
                        is VrapArrayType -> when (s.itemType) {
                            is ObjectType -> true
                            else -> false
                        }
                        else -> false
                    }
                }
                .filter { it.namespaceName() != vrapType.namespaceName()}
                .map { getImportsForType(it) }
                .filterNotNull()
                .filter { !it.equals("\\\\") }
                .sortedBy { it }
                .distinct()
                .toList()

        return result
    }
}

fun getImportsForType(vrapType: VrapType): String? {
    return when (vrapType) {
        is VrapObjectType -> "${vrapType.namespaceName()}\\${vrapType.simpleName()}"
        is VrapArrayType -> when (vrapType.itemType) {
            is VrapObjectType -> "${vrapType.namespaceName()}\\${vrapType.simpleName()}"
            else -> null
        }
        else -> null

    }
}

fun ObjectType.hasSubtypes(): Boolean = this.discriminator?.isNotBlank()?:false && (this.subTypes?.isNotEmpty() ?: false)
