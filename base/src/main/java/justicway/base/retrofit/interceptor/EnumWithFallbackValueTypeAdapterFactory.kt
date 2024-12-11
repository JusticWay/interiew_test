package justicway.base.retrofit.interceptor

import com.google.gson.Gson
import com.google.gson.JsonPrimitive
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

object EnumWithFallbackValueTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (!type.rawType.isEnum) {
            return null
        }

        val candidates = type.rawType.fields
            .asSequence()
            .filter { it.type == type.rawType && it.isAnnotationPresent(FallbackValue::class.java) }
            .map { field ->
                @Suppress("UNCHECKED_CAST")
                type.rawType.enumConstants.single { enumValue ->
                    field.get(null) == enumValue
                } as T
            }
            .toList()

        val delegate = gson.getDelegateAdapter(this, type)

        return when {
            candidates.isEmpty() -> delegate
            candidates.size > 1 -> throw IllegalArgumentException("Only one enum value can be annotated with @${FallbackValue::class.java.simpleName}")
            else -> {
                val fallbackValue = candidates.single()
                object : TypeAdapter<T>() {
                    @Throws(IOException::class)
                    override fun write(writer: JsonWriter, value: T) {
                        delegate.write(writer, value)
                    }

                    @Throws(IOException::class)
                    override fun read(reader: JsonReader): T {
                        // Keep null as undefined
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull() // consume
                            @Suppress("UNCHECKED_CAST")
                            return null as T
                        }
                        val rawString = reader.nextString()
                        val fromDelegate = delegate.fromJsonTree(JsonPrimitive(rawString))
                        return if (fromDelegate != null) {
                            fromDelegate
                        } else {
                            fallbackValue
                        }
                    }
                }
            }
        }
    }
}