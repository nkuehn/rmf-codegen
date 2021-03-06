package io.vrap.codegen.languages.ramldoc.model

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.vrap.rmf.codegen.rendring.*

class RamldocModelModule : AbstractModule() {
    override fun configure() {
        val objectTypeBinder = Multibinder.newSetBinder(binder(), ObjectTypeRenderer::class.java)
        objectTypeBinder.addBinding().to(RamlObjectTypeRenderer::class.java)

        val stringTypeBinder = Multibinder.newSetBinder(binder(), StringTypeRenderer::class.java)
        stringTypeBinder.addBinding().to(RamlScalarTypeRenderer::class.java)

        val patternStringTypeBinder = Multibinder.newSetBinder(binder(), PatternStringTypeRenderer::class.java)
        patternStringTypeBinder.addBinding().to(RamlScalarTypeRenderer::class.java)

        val namedScalarTypeBinder = Multibinder.newSetBinder(binder(), NamedScalarTypeRenderer::class.java)
        namedScalarTypeBinder.addBinding().to(RamlScalarTypeRenderer::class.java)

        val resourceBinder = Multibinder.newSetBinder(binder(), ResourceRenderer::class.java)
        resourceBinder.addBinding().to(RamlResourceRenderer::class.java)

        val fileBinder = Multibinder.newSetBinder(binder(), FileProducer::class.java)
        fileBinder.addBinding().to(ApiRamlRenderer::class.java)
        fileBinder.addBinding().to(RamlExampleRenderer::class.java)
    }
}
