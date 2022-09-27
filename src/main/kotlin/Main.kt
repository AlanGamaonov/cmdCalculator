import java.lang.Exception

/**
 * Входные данные: строка вида 1+2+3 (с пробелами или без)
 * Поддерживаются операции: +, -, *, /, (, )
 *
 * Чтобы добавить новый оператор, достаточно унаследоваться от абстрактного класса [Operator],
 * указать его ассоциативность [Operator.align], приоритет [Operator.precedence] (меньше число - выше приоритет) и обозначение [Operator.name]
 *
 * Прошу учесть, что поддерживаются только бинарные (принимающие два аргумента) операторы, работающие с типом [Double]
 *
 * (Для борьбы с унарными минусами и плюсами были изобретены костыли, с.м. [StringExpressionParser.parseExpression])
 */
fun main() {
    try {
        val parsed = StringExpressionParser.parseExpression(readln())
        val answer = RpnCalculator.calcRpnQueue(parsed)
        println(answer)
    } catch (e: Exception) {
        println("Something is wrong!")
        println(e.message)
    }
}

// Бизнес-исключения (сообщения для пользователя)
class InvalidInputException : IllegalArgumentException("Invalid input")
class MismatchedParenthesesException : IllegalArgumentException("Mismatched parentheses")