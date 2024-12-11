@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package justicway.base.retrofit.interceptor

import android.text.TextUtils
import android.util.Log
import okhttp3.Request
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Tony Shen on 2017/7/9.
 */
class Logger {

    companion object {
        private const val JSON_INDENT = 3
        private const val MAX_LONG_SIZE = 120
        private const val N = "\n"
        private const val T = "\t"
        private const val TOP_LEFT_CORNER = '╔'
        private const val BOTTOM_LEFT_CORNER = '╚'
        private const val DOUBLE_DIVIDER = "═════════════════════════════════════════════════"
        private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val LINE_SEPARATOR = System.getProperty("line.separator")
        private fun String.isLineEmpty() =
            isEmpty() || N == this || T == this || this.trim { it <= ' ' }.isEmpty()

        private fun getDoubleSeparator(hideVerticalLine: Boolean = false): String =
            if (hideVerticalLine) LINE_SEPARATOR + " " + LINE_SEPARATOR else LINE_SEPARATOR + "║ " + LINE_SEPARATOR

        /**
         * 支持超长日志的打印
         */
        private fun printLog(tag: String, logString: String) {
            if (logString.length > 4000) {
                var i = 0
                while (i < logString.length) {
                    if (i + 4000 < logString.length)
                        Log.i(tag, logString.substring(i, i + 4000))
                    else
                        Log.i(tag, logString.substring(i, logString.length))
                    i += 4000
                }
            } else
                Log.i(tag, logString)
        }

        @JvmStatic
        fun printJsonRequest(
            builder: LoggingInterceptor.Builder,
            request: Request,
            headerString: String
        ) {
            val hideVerticalLine = builder.hideVerticalLineFlag
            val sb = StringBuilder()
            sb.append("  ").append(LINE_SEPARATOR).append(TOP_BORDER).append(LINE_SEPARATOR)
            sb.append(getRequest(request, headerString, hideVerticalLine))
            if (request.method != "GET") { // get请求不需要body
                val requestBody = if (hideVerticalLine) {
                    " $LINE_SEPARATOR Body:$LINE_SEPARATOR"
                } else {
                    "║ ${LINE_SEPARATOR}║ Body:$LINE_SEPARATOR"
                }
                val bodyString = bodyToString(request).split(LINE_SEPARATOR!!.toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()
                sb.append(requestBody + logLines(bodyString, hideVerticalLine))
            } else {
                val header = request.headers.toString()
                if (header.isLineEmpty()) {
                    sb.append(LINE_SEPARATOR)
                }
            }
            sb.append(BOTTOM_BORDER)
            Log.i(builder.getTag(true), sb.toString())
        }

        @JvmStatic
        fun printFileRequest(
            builder: LoggingInterceptor.Builder,
            request: Request,
            headerString: String
        ) {
            val sb = StringBuilder()
            sb.append("  ").append(LINE_SEPARATOR).append(TOP_BORDER).append(LINE_SEPARATOR)
            sb.append(getRequest(request, headerString))
            val requestBody = "║ $LINE_SEPARATOR"
            val binaryBodyString = binaryBodyToString(request).split(LINE_SEPARATOR!!.toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
            sb.append(requestBody + logLines(binaryBodyString))
            sb.append(BOTTOM_BORDER)
            Log.i(builder.getTag(true), sb.toString())
        }

        @JvmStatic
        fun printJsonResponse(
            builder: LoggingInterceptor.Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            bodyString: String,
            segments: List<String>,
            request: Request
        ) {
            val hideVerticalLine = builder.hideVerticalLineFlag
            val sb = StringBuilder()
            sb.append("  ").append(LINE_SEPARATOR).append(TOP_BORDER).append(LINE_SEPARATOR)
            sb.append(
                getResponse(
                    headers,
                    chainMs,
                    code,
                    isSuccessful,
                    segments,
                    hideVerticalLine,
                    request
                )
            )
            val responseBody = if (hideVerticalLine) {
                " $LINE_SEPARATOR Body:$LINE_SEPARATOR"
            } else {
                "║ ${LINE_SEPARATOR}║ Body:$LINE_SEPARATOR"
            }
            val bodyStrings = getJsonString(bodyString).split(LINE_SEPARATOR!!.toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
            sb.append(responseBody + logLines(bodyStrings, hideVerticalLine))
            sb.append(BOTTOM_BORDER)
            printLog(builder.getTag(false), sb.toString())
        }

        @JvmStatic
        fun printFileResponse(
            builder: LoggingInterceptor.Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            segments: List<String>,
            request: Request
        ) {
            val sb = StringBuilder()
            sb.append("  ").append(LINE_SEPARATOR).append(TOP_BORDER).append(LINE_SEPARATOR)
            sb.append(getResponse(headers, chainMs, code, isSuccessful, segments, false, request))
            sb.append(BOTTOM_BORDER)
            Log.i(builder.getTag(false), sb.toString())
        }

        private fun getRequest(
            request: Request,
            headerString: String,
            hideVerticalLine: Boolean = false
        ) = with(headerString) {
            if (hideVerticalLine) {
                " URL: " + request.url + getDoubleSeparator(hideVerticalLine) + " Method: @" + request.method + getDoubleSeparator(
                    hideVerticalLine
                ) +
                        if (isLineEmpty()) " " else " Headers:" + LINE_SEPARATOR + dotHeaders(
                            headerString,
                            hideVerticalLine
                        )
            } else {
                "║ URL: " + request.url + getDoubleSeparator() + "║ Method: @" + request.method + getDoubleSeparator() +
                        if (isLineEmpty()) "║ " else "║ Headers:" + LINE_SEPARATOR + dotHeaders(
                            headerString
                        )
            }
        }

        private fun getResponse(
            header: String,
            tookMs: Long,
            code: Int,
            isSuccessful: Boolean,
            segments: List<String>,
            hideVerticalLine: Boolean = false,
            request: Request
        ): String {
            val segmentString: String?
            return if (hideVerticalLine) {
                segmentString = " " + slashSegments(segments)
                " URL: " + request.url + (if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : " + isSuccessful + " - " + "Received in: " + tookMs + "ms" + getDoubleSeparator(
                    hideVerticalLine
                ) + " Status Code: " +
                        code + getDoubleSeparator(hideVerticalLine) +
                        if (header.isLineEmpty()) " " else " Headers:" + LINE_SEPARATOR + dotHeaders(
                            header,
                            hideVerticalLine
                        )
            } else {
                segmentString = "║ " + slashSegments(segments)

                "║ URL: " + request.url + (if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : " + isSuccessful + " - " + "Received in: " + tookMs + "ms" + getDoubleSeparator() + "║ Status Code: " +
                        code + getDoubleSeparator() +
                        if (header.isLineEmpty()) "║ " else "║ Headers:" + LINE_SEPARATOR + dotHeaders(
                            header
                        )
            }
        }

        private fun slashSegments(segments: List<String>): String {
            val segmentString = StringBuilder()
            for (segment in segments) {
                segmentString.append("/").append(segment)
            }
            return segmentString.toString()
        }

        private fun dotHeaders(header: String, hideVerticalLine: Boolean = false): String {
            val headers = header.split(LINE_SEPARATOR!!.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val builder = StringBuilder()
            if (headers.isNotEmpty()) {
                for (item in headers) {
                    if (hideVerticalLine) {
                        builder.append(" - ").append(item).append("\n")
                    } else {
                        builder.append("║ - ").append(item).append("\n")
                    }
                }
            } else {
                builder.append(LINE_SEPARATOR)
            }
            return builder.toString()
        }

        private fun logLines(lines: Array<String>, hideVerticalLine: Boolean = false): String {
            val sb = StringBuilder()
            for (line in lines) {
                val lineLength = line.length
                for (i in 0..lineLength / MAX_LONG_SIZE) {
                    val start = i * MAX_LONG_SIZE
                    var end = (i + 1) * MAX_LONG_SIZE
                    end = if (end > line.length) line.length else end

                    if (hideVerticalLine) {
                        sb.append(" " + line.substring(start, end)).append(LINE_SEPARATOR)
                    } else {
                        sb.append("║ " + line.substring(start, end)).append(LINE_SEPARATOR)
                    }

                }
            }
            return sb.toString()
        }

        private fun bodyToString(request: Request): String {
            try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                if (copy.body == null) return ""

                copy.body!!.writeTo(buffer)
                return getJsonString(buffer.readUtf8())
            } catch (e: IOException) {
                return "{\"err\": \"" + e.message + "\"}"
            }
        }

        private fun binaryBodyToString(request: Request): String {
            val requestBody = request.newBuilder().build().body ?: return ""
            var buffer: String?
            buffer = if (requestBody.contentType() != null) {
                "Content-Type: " + requestBody.contentType().toString()
            } else {
                ""
            }
            if (requestBody.contentLength() > 0) {
                buffer += LINE_SEPARATOR + "Content-Length: " + requestBody.contentLength()
            }
            return buffer
        }

        @JvmStatic
        fun getJsonString(msg: String): String {
            val message: String
            message = try {
                when {
                    msg.startsWith("{") -> {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(JSON_INDENT)
                    }

                    msg.startsWith("[") -> {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(JSON_INDENT)
                    }

                    else -> msg
                }
            } catch (e: JSONException) {
                msg
            }
            return message
        }
    }
}