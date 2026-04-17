package eu.wynq.arkascendedservermanager.core.server

import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class DefaultString(val value: String = "")

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class DefaultInt(val value: Int = 0)


val KProperty<*>.defaultValue: Any?
    get() {
        return this.findAnnotation<DefaultString>()?.value
            ?: this.javaField?.getAnnotation(DefaultString::class.java)?.value
            ?: this.findAnnotation<DefaultInt>()?.value
            ?: this.javaField?.getAnnotation(DefaultInt::class.java)?.value
    }

val KProperty<*>.defaultValueString: String?
    get() = this.findAnnotation<DefaultString>()?.value
        ?: this.javaField?.getAnnotation(DefaultString::class.java)?.value

val KProperty<*>.defaultValueInt: Int?
    get() = this.findAnnotation<DefaultInt>()?.value
        ?: this.javaField?.getAnnotation(DefaultInt::class.java)?.value
