package justicway.base.retrofit.interceptor

data class ShellCommand(
    val name: String,
    val arguments: List<Pair<String, String>>
) {
    fun argument(name: String, value: String): ShellCommand {
        return copy(arguments = arguments + Pair(name, value))
    }
    fun argument(arg: Pair<String, String>): ShellCommand {
        return copy(arguments = arguments + arg)
    }

    override fun toString(): String {
        return arguments.fold(name) { prev, el ->
            "$prev \\\n${el.first} ${el.second}"
        }
    }
}