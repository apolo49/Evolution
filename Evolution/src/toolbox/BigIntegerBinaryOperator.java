package toolbox;

import java.math.BigInteger;

@FunctionalInterface

/**
 * Represents an operation upon two {@code BigInteger}-valued operands and producing a
 * {@code BigInteger}-valued result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsBigInteger(BigInteger, BigInteger)}.
 *
 * @see BinaryOperator
 * @see LongUnaryOperator
 **/

public interface BigIntegerBinaryOperator{
	
    /**
     * Applies this operator to the given operands.
     *
     * @param left the first operand
     * @param right the second operand
     * @return the operator result
     */
	
	BigInteger ApplyAsBigInteger(BigInteger right, BigInteger left);
}
