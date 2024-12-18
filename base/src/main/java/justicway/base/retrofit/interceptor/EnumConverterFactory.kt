package justicway.base.retrofit.interceptor

import com.google.gson.annotations.SerializedName
import retrofit2.Converter
import java.lang.reflect.Type
import retrofit2.Retrofit

class EnumConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type is Class<*> && type.isEnum) {
            return Converter<Any?, String> { value -> getSerializedNameValue(value as Enum<*>) }
        }
        return null
    }
}

fun <E : Enum<*>> getSerializedNameValue(e: E): String {
    try {
        return e.javaClass.getField(e.name).getAnnotation(SerializedName::class.java)?.value ?: "0"
    } catch (exception: NoSuchFieldException) {
        exception.printStackTrace()
    }

    return ""
}