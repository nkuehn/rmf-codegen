package io.vrap.codegen.languages.php.jms

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.extensions.hasSubtypes
import io.vrap.codegen.languages.extensions.namedSubTypes
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.rendring.utils.keepIndentation
import io.vrap.rmf.codegen.types.VrapArrayType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property

class JmsTypeRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {


    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun render(type: ObjectType): TemplateFile {


        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName().escapeAll()};
            |
            |<<${type.imports(vrapType.namespaceName())}>>
            |use JMS\\Serializer\\Annotation\\Type;
            |${if (type.hasSubtypes()) "use JMS\\\\Serializer\\\\Annotation\\\\Discriminator;" else ""}
            |
            |<<${type.subTypesAnnotations()}>>
            |final class ${vrapType.simpleClassName}Model implements ${vrapType.simpleClassName}
            |{
            |
            |    <<${type.toBeanFields()}>>
            |
            |    <<${type.getters()}>>
            |    <<${type.setters()}>>
            |
            |}
        """.trimMargin().keepIndentation("<<", ">>").forcedLiteralEscape()


        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Model.php",
                content = content
        )
    }

    fun ObjectType.setters() = this.allProperties
            .map { it.setter() }
            .joinToString(separator = "\n\n")


    fun ObjectType.getters() = this.allProperties
            .map { it.getter() }
            .joinToString(separator = "\n\n")

    fun Property.setter(): String {
        return if (this.isPatternProperty()) {

            """
            |/**
            | *
            | */
            |public function setValue(string $!key, ${this.type.toVrapType().simpleName()} $!value)
            |{
            |    if ($!this->values == null) {
            |        $!this->values = [];
            |    }
            |    $!this->values[$!key] = $!value;
            |}
            """.trimMargin()
        } else {
            """
            |/**
            | * @var ${this.type.toVrapType().simpleName()} $${this.name}
            | * @return $!this
            | */
            |public function set${this.name.capitalize()}(${if (this.type.toVrapType().isScalar() and (this.type.toVrapType() is VrapArrayType)) "array" else {"${this.type.toVrapType().simpleName()}"}} $${this.name})
            |{
            |   $!this->${this.name} = $${this.name};
            |   return $!this;
            |}
            """.trimMargin()
        }
    }

    fun Property.getter(): String {
        return if (this.isPatternProperty()) {

            """
                |/**
                | ${this.type.toPhpComment()}
                | */
                |public function getValues()
                |{
                |    return $!this->values;
                |}
            """.trimMargin()
        } else {
            """
                |/**
                | * @return ${this.type.toVrapType().simpleName()}
                | */
                |public function get${this.name.capitalize()}()
                |{
                |   return $!this->${this.name};
                |}
        """.trimMargin()
        }
    }

    fun ObjectType.imports(namespace: String) = this.getImports(this.allProperties)
//            .filter { !it.toNamespaceDir().equals(namespace) }
            .map { "use ${it.escapeAll()};" }.joinToString(separator = "\n")

    fun ObjectType.toBeanFields() = this.allProperties
            .filter { property -> !property.isPatternProperty() }
            .map { it.toPhpField() }.joinToString(separator = "\n\n")


    fun Property.toPhpField(): String {

        val toVrapType = this.type.toVrapType()
        val typeName = when(toVrapType) {
            is VrapObjectType ->  "${toVrapType.fullClassName()}Model"
            is VrapArrayType -> {
                if (toVrapType.itemType.isScalar()) {
                    "array"
                } else {
                    toVrapType.fullClassName()
                }
            }
            else -> toVrapType.fullClassName()

        }

        return """
            |/**
            | * @Type("${typeName.escapeAll()}")
            | * @var ${toVrapType.simpleName()}
            | */
            |protected $${this.name};
        """.trimMargin();
    }

    fun Property.isPatternProperty() = this.name.startsWith("/") && this.name.endsWith("/")

    fun ObjectType.constructor(): String {
        return if (this.discriminator != null)
            """
            |/**
            | * @param array $!data
            | */
            |public function __construct(array $!data = []) {
            |    parent::__construct($!data);
            |    $!this->set${this.discriminator.capitalize()}(static::DISCRIMINATOR_VALUE);
            |}
            """.trimMargin()
        else
            ""
    }

    fun ObjectType.subTypesAnnotations(): String {
        return if (hasSubtypes())
            """
            |/**
            | * @Discriminator(
            | *  field = "${this.discriminator}",
            | *  map = {
            | *    "" : "${this.toVrapType().fullClassName().escapeAll()}",
            | *    ${namedSubTypes().map { "\"${(it as ObjectType).discriminatorValue}\": \"${it.toVrapType().fullClassName().escapeAll()}\"" }.joinToString(separator = ",\n *    ")}
            | *  })
            | */
            """.trimMargin()
        else
            ""
    }

}