package justicway.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun Context?.toast(message : String){
    this?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}

fun Long?.toTime(
    format: String = "yyyy-MM-dd HH:mm:ss",
    local: Locale = Locale.getDefault()
): String {
    return try {
        val sdf = SimpleDateFormat(format, local)
        val netDate = Date(this ?: 0)
        sdf.format(netDate)
    } catch (e: Exception) {
        ""
    }
}

fun String?.safeToDouble(): Double {
    return try {
        this?.toDouble() ?: 0.0
    } catch (e: NumberFormatException) {
        0.0
    }
}

fun String?.safeToLong(): Long {
    return try {
        this?.toLong() ?: 0
    } catch (e: NumberFormatException) {
        0
    }
}

fun Long?.toUnit(): String {
    return if(this == null) "0" else when {
        this < 10000 -> {
            this.toString()
        }
        this < 100000000 -> {
            "${(this / 10000)}萬"
        }
        else -> {
            "${(this / 100000000)}億"
        }
    }
}

/**
 * hideKeyboard
 */
fun Fragment.hideKeyboard() {
    activity?.apply {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

/**
 * dp and px conversion
 */
// Convert px to dp
val Int.toDpValue: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp

/**
 * dp and px conversion
 */
//Convert dp to px
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.toDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp


@Composable
fun Modifier.shimmerEffect(colors: List<Color> = listOf(Color(0xFFB8B5B5), Color(0xFF8F8B8B), Color(0xFFB8B5B5))): Modifier =
    composed {
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        val transition = rememberInfiniteTransition()
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(2000)
            )
        )

        background(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        )
            .onGloballyPositioned {
                size = it.size
            }
    }