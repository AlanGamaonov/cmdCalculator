import java.util.*

class RpnCalculator {
    companion object {
        /**
         * Shunting yard algorithm. Строится RPN, которая вычисляется по ходу работы
         * @see <a href="https://en.wikipedia.org/wiki/Shunting_yard_algorithm">Shunting yard algorithm</a>
         * @see <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">(RPN) Reverse Polish notation</a>
         */
        fun calcRpnQueue(source: List<ExprMember>): Double {
            val result = mutableListOf<Term>()
            val operatorStack = Stack<Operator>()
            source.forEach {
                when (it) {
                    is Term -> result.add(it)
                    is LeftParenthesisOperator -> operatorStack.push(it)
                    is RightParenthesisOperator -> {
                        if (operatorStack.isEmpty()) throw InvalidInputException()
                        try {
                            while ((operatorStack.peek() is LeftParenthesisOperator).not()) result.eval(operatorStack.pop())
                            operatorStack.pop()
                        } catch (e: EmptyStackException) {
                            throw MismatchedParenthesesException()
                        }
                    }
                    is Operator -> {
                        while (
                            operatorStack.isNotEmpty()
                            && (operatorStack.peek() is LeftParenthesisOperator).not()
                            && (operatorStack.peek().precedence < it.precedence
                                    || operatorStack.peek().precedence == it.precedence && it.align == Align.LEFT)
                        ) {
                            result.eval(operatorStack.pop())
                        }
                        operatorStack.push(it)
                    }
                    else -> throw IllegalStateException()
                }
            }
            operatorStack.reversed().forEach { operator ->
                if (operator is LeftParenthesisOperator || operator is RightParenthesisOperator)
                    throw MismatchedParenthesesException()
                result.eval(operator)
            }
            if (result.size != 1) throw InvalidInputException()
            return result.first().value
        }

        private fun MutableList<Term>.eval(operator: Operator) {
            val second = this.removeLastOrNull()?.value
            val first = if (operator is UnaryPlusOperator) null else this.removeLastOrNull()?.value
            this.add(Term(operator.eval(first, second)))
        }
    }
}