import kotlin.math.pow

val availableOperators = listOf(
    PlusOperator(),
    MinusOperator(),
    ProductOperator(),
    DivisionOperator(),
    PowerOperator(),
    LeftParenthesisOperator(),
    RightParenthesisOperator(),
)

enum class Align {
    NONE,
    LEFT,
    RIGHT,
}

/**
 * Член выражения. Обобщение для операторов и термов
 */
interface ExprMember {
    val precedence: Int
    val name: String
    val align: Align
}

abstract class Operator : ExprMember {
    override val align: Align = Align.LEFT
    abstract fun eval(left: Double?, right: Double?) : Double
}

/**
 * Просто обёртка для Double'а. Можно было бы обобщить дженериком, но в ТЗ только даблы
 */
data class Term(val value: Double) : ExprMember {
    override val align: Align = Align.NONE
    override val precedence: Int = 0
    override val name: String = value.toString()
}

data class PlusOperator(
    override val precedence: Int = 4,
    override val name: String = "+",
) : Operator() {
    override fun eval(left: Double?, right: Double?) = (left ?: 0.0).plus(right ?: throw InvalidInputException())
}

data class MinusOperator(
    override val precedence: Int = 4,
    override val name: String = "-"
) : Operator() {
    override fun eval(left: Double?, right: Double?) : Double {
        if (left == null || right == null) throw InvalidInputException()
        return left.minus(right)
    }
}

data class ProductOperator(
    override val precedence: Int = 3,
    override val name: String = "*"
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double =
        if (left == null || right == null) throw InvalidInputException()
        else left.times(right)
}

data class DivisionOperator(
    override val precedence: Int = 3,
    override val name: String = "/"
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double =
        if (left == null || right == null) throw InvalidInputException()
        else left.div(right)
}

data class PowerOperator(
    override val align: Align = Align.RIGHT,
    override val precedence: Int = 2,
    override val name: String = "^"
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double =
        if (left == null || right == null) throw InvalidInputException()
        else left.pow(right)
}

data class LeftParenthesisOperator(
    override val align: Align = Align.NONE,
    override val precedence: Int = 1,
    override val name: String = "("
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double = throw InvalidInputException()
}

data class RightParenthesisOperator(
    override val align: Align = Align.NONE,
    override val precedence: Int = 1,
    override val name: String = ")"
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double = throw InvalidInputException()
}

// Это костыль :(
data class UnaryPlusOperator(
    override val align: Align = Align.RIGHT,
    override val precedence: Int = 2,
    override val name: String = ""
) : Operator() {
    override fun eval(left: Double?, right: Double?): Double {
        if (right == null) throw InvalidInputException()
        return right
    }
}