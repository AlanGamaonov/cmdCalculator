
class StringExpressionParser {
    companion object {
        fun parseExpression(raw: String) : List<ExprMember> {
            // Далеко не самое изящное решение, но приемлемое и достаточно дешёвое с точки зрения разработки
            val prepared = raw.replace(Regex("-?[0-9]{1,13}(\\.[0-9]*)?")) { ";${it.value};" }

            return prepared
                .split(';')
                .filter { it.isNotEmpty() }
                .flatMap {
                    val num = it.toDoubleOrNull()
                    if (num != null) {
                        if (num >= 0) listOf(Term(num))
                        else listOf(UnaryPlusOperator(), Term(num))
                    }
                    else getOperator(it)
                }
        }

        private fun getOperator(raw: String) : List<ExprMember> {
            var prepared = raw
            //Для кейсов -1*-3*(-3*-3)
            //проблемное место: ...*(...
            availableOperators.map { it.name }.forEach { op ->
                prepared = prepared.replace(op, ";$op;")
            }
            return prepared.split(';').filter { it.isNotBlank() }.flatMap { op ->
                val matchingOperators = availableOperators.filter { it.name == op }
                if (matchingOperators.size > 1)
                    throw IllegalStateException("Operator indeterminacy: operators " +
                            "[${matchingOperators.joinToString(" ") { it::class.simpleName!! }}] have the same name")
                if (matchingOperators.isEmpty())
                    throw IllegalStateException("No operator wrapper for name \"$op\"")
                matchingOperators
            }
        }

    }
}