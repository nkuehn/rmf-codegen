package io.vrap.codegen.languages.php.jms

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ObjectType

class JmsCollectionRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @Named(io.vrap.rmf.codegen.di.VrapConstants.BASE_PACKAGE_NAME)
    lateinit var packagePrefix:String

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName()};
            |
            |use Doctrine\Common\Collections\ArrayCollection;
            |
            |class ${vrapType.simpleClassName}Collection extends ArrayCollection
            |{
            |}
        """.trimMargin().forcedLiteralEscape()


        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Collection.php",
                content = content
        )
    }
}