package io.vrap.rmf.codegen.di

import com.google.inject.BindingAnnotation


@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultPackage

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OutputFolder

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ClientPackageName


@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelPackageName

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BasePackageName

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiGitHash


@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SharedPackageName

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumStringTypes

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PatternStringTypes

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedScalarTypes

@BindingAnnotation
@Target(AnnotationTarget.FIELD,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllAnyTypes
