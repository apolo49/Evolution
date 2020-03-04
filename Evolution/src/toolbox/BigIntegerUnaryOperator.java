package toolbox;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents an operation on a single {@code BigInteger}-valued operand that produces
 * a {@code BigInteger}-valued result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsBigInteger(BigInteger)}.
 *
 **/

@FunctionalInterface
public interface BigIntegerUnaryOperator {
	
    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return The operator result
     */
	BigInteger applyAsBigInteger(BigInteger operand);
	
    /**
     * Returns a composed operator that first applies the {@code before}
     * operator to its input, and then applies this operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param before the operator to apply before this operator is applied
     * @return a composed operator that first applies the {@code before}
     * operator and then applies this operator
     * @throws NullPointerException if before is null
     *
     * @see #andThen(BigIntegerUnaryOperator)
     */
	
	default BigIntegerUnaryOperator compose(BigIntegerUnaryOperator before) {
        Objects.requireNonNull(before);
        return (BigInteger v) -> applyAsBigInteger(before.applyAsBigInteger(v));
    }
	
	/**
     * Returns a composed operator that first applies this operator to
     * its input, and then applies the {@code after} operator to the result.
     * If evaluation of either operator throws an exception, it is relayed to
     * the caller of the composed operator.
     *
     * @param after the operator to apply after this operator is applied
     * @return a composed operator that first applies this operator and then
     * applies the {@code after} operator
     * @throws NullPointerException if after is null
     *
     * @see #compose(BigIntegerUnaryOperator)
     */
	
	default BigIntegerUnaryOperator andThen(BigIntegerUnaryOperator after) {
		Objects.requireNonNull(after);
        return (BigInteger t) -> after.applyAsBigInteger(applyAsBigInteger(t));
	}
	
    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @return a unary operator that always returns its input argument
     */
	static BigIntegerUnaryOperator identity() {
        return t -> t;
    }
	
}
