package justicway.base

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import justicway.base.BuildConfig

fun Any?.log(tag: String = "JustWay", lv: Int = 1): Any? {
    if (BuildConfig.DEBUG) when (lv) {
        0 -> {
            Log.v(tag, toString())
        }

        1 -> {
            Log.d(tag, toString())
        }

        2 -> {
            Log.i(tag, toString())
        }

        3 -> {
            Log.w(tag, toString())
        }

        4 -> {
            Log.e(tag, toString())
        }
    }
    return this
}

// null entity
inline fun <T> T?.nullCase(block: () -> Unit): T? {
    if (this == null) block()
    return this@nullCase
}

// has entity
inline fun <T> T?.hasCase(block: (T) -> Unit): T? {
    this?.let(block)
    return this@hasCase
}

// get entity or default
fun <T> T?.default(defaultValue: T): T {
    return this ?: defaultValue
}

fun Fragment?.navigateBack(): Unit {
    this?.findNavController()?.previousBackStackEntry
        .hasCase {
            this?.findNavController()?.navigateUp()
        }.nullCase {
            this?.requireActivity()?.finish()
        }
}