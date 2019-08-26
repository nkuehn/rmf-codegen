package io.vrap.codegen.languages.php.model;

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vrap.codegen.languages.php.PhpBaseTypes
import io.vrap.codegen.languages.php.PhpSubTemplates
import io.vrap.codegen.languages.php.extensions.*
import io.vrap.rmf.codegen.di.BasePackageName
import io.vrap.rmf.codegen.io.TemplateFile
import io.vrap.rmf.codegen.rendring.ObjectTypeRenderer
import io.vrap.rmf.codegen.rendring.utils.escapeAll
import io.vrap.rmf.codegen.types.VrapEnumType
import io.vrap.rmf.codegen.types.VrapObjectType
import io.vrap.rmf.codegen.types.VrapTypeProvider
import io.vrap.rmf.raml.model.types.ArrayType
import io.vrap.rmf.raml.model.types.ObjectType
import io.vrap.rmf.raml.model.types.Property
import io.vrap.rmf.raml.model.types.util.TypesSwitch
import org.eclipse.emf.ecore.EObject
import java.util.*

class PhpCollectionRenderer @Inject constructor(override val vrapTypeProvider: VrapTypeProvider) : ObjectTypeExtensions, EObjectTypeExtensions, ObjectTypeRenderer {

    @Inject
    @BasePackageName
    lateinit var packagePrefix:String

    override fun render(type: ObjectType): TemplateFile {

        val vrapType = vrapTypeProvider.doSwitch(type) as VrapObjectType

        val content = """
            |<?php
            |${PhpSubTemplates.generatorInfo}
            |namespace ${vrapType.namespaceName()};
            |
            |use ${packagePrefix.toNamespaceName()}\Base\MapperSequence;
            |use ${packagePrefix.toNamespaceName()}\Exception\InvalidArgumentException;
            |use stdClass;
            |
            |/**
            | * @extends MapperSequence<${ vrapType.simpleClassName }>
            | * @method ${vrapType.simpleClassName} current()
            | * @method ${vrapType.simpleClassName} at($!offset)
            | */
            |class ${vrapType.simpleClassName}Collection extends MapperSequence
            |{
            |    /**
            |     * @psalm-assert ${vrapType.simpleClassName} $!value
            |     * @psalm-param ${vrapType.simpleClassName}|stdClass $!value
            |     * @return ${vrapType.simpleClassName}Collection
            |     * @throws InvalidArgumentException
            |     */
            |    public function add($!value)
            |    {
            |        if (!$!value instanceof ${vrapType.simpleClassName}) {
            |            throw new InvalidArgumentException();
            |        }
            |        $!this->store($!value);
            |
            |        return $!this;
            |    }
            |
            |    /**
            |     * @psalm-return callable(int):?${vrapType.simpleClassName}
            |     */
            |    protected function mapper()
            |    {
            |        return function(int $!index): ?${vrapType.simpleClassName} {
            |            $!data = $!this->get($!index);
            |            if ($!data instanceof stdClass) {
            |                $!data = new ${vrapType.simpleName()}Model($!data);
            |                $!this->set($!data, $!index);
            |            }
            |            return $!data;
            |        };
            |    }
            |}
        """.trimMargin().forcedLiteralEscape()

        return TemplateFile(
                relativePath = "src/" + vrapType.fullClassName().replace(packagePrefix.toNamespaceName(), "").replace("\\", "/") + "Collection.php",
                content = content
        )
    }
}
