package toolbox;

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@code BigInteger} value that may be updated atomically.  See the
 * {@link java.util.concurrent.atomic} package specification for
 * description of the properties of atomic variables. An
 * {@code AtomicBigInteger} is used in applications such as atomically
 * incremented sequence numbers, and cannot be used as a replacement
 * for a {@link java.math.BigInteger}. However, this class does extend
 * {@code Number} to allow uniform access by tools and utilities that
 * deal with numerically-based classes.
 **/

public final class AtomicBigInteger extends Number implements java.io.Serializable{

	private static final long serialVersionUID = 1354208247948225650L;

    private final AtomicReference<BigInteger> atomicRefValue;
    
	private volatile BigInteger value;

	/**
	 * Creates a new AtomicBigInteger with the given initial value.
	 * 
	 * @param initialValue Initial Value of the Atomic Big Integer
	 **/
	
	public AtomicBigInteger(BigInteger initialValue) {
        value = initialValue;
        this.atomicRefValue = new AtomicReference<>(Objects.requireNonNull(initialValue)); 
    }
	
	/**
	 * Sets to the {@code newValue}
	 * 
	 * @param newValue, the new value of the Atomic Big Integer
	 **/
	
	public final void set(BigInteger newValue) {
        value = newValue;
        atomicRefValue.set(newValue);
    }
	
	/**
	 * Atomically increments by one the current value.
	 * 
	 * @return the previous value
	 **/
	
	public BigInteger getAndIncrement() {
		return atomicRefValue.getAndAccumulate(BigInteger.ONE, (previous, x) -> previous.add(x));
	}
	
	/**
	 * Get the current value
	 * 
	 * @return the value of the atomicBigInteger as a BigInteger
	 **/
	
	public BigInteger get() {
		return value;
	}
	
	/**
     * Atomically updates the current value with the results of
     * applying the given function, returning the previous value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the previous value
	 **/
	
	public final BigInteger getAndUpdate(BigIntegerUnaryOperator updateFunction) {
        BigInteger prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsBigInteger(prev); //BigIntegerBinaryOperator.java, line 28 
        } while (!compareAndSet(prev, next));
        return prev;
    }
	
	/**
     * Atomically updates the current value with the results of
     * applying the given function, returning the updated value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the updated value
	 **/
	
	public final BigInteger updateAndGet(BigIntegerUnaryOperator updateFunction) {
        BigInteger prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsBigInteger(prev); //BigIntegerBinaryOperator.java, line 28 
        } while (!compareAndSet(prev, next));
        return next;
    }
	
	/**
     * Atomically updates the current value with the results of
     * applying the given function to the current and given values,
     * returning the previous value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function
     * is applied with the current value as its first argument,
     * and the given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the previous value
	 **/
	
	public final BigInteger getAndAccumulate(BigInteger x, BigIntegerBinaryOperator accumulatorFunction) {
		BigInteger prev, next;
		do {
			prev = get();
			next = accumulatorFunction.ApplyAsBigInteger(prev, x); //BigIntegerBinaryOperator.java, line 28 
		} while (!compareAndSet(prev, next));
			return prev;
		}
	
	/**
     * Atomically updates the current value with the results of
     * applying the given function to the current and given values,
     * returning the updated value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function
     * is applied with the current value as its first argument,
     * and the given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the updated value
	 **/
  
	public final BigInteger accumulateAndGet(BigInteger x, BigIntegerBinaryOperator accumulatorFunction) {
		BigInteger prev, next;
		do {
			prev = get();
			next = accumulatorFunction.ApplyAsBigInteger(prev, x); //BigIntegerBinaryOperator.java, line 28 
		}while (!compareAndSet(prev, next));
			return next;
		}

	@Override
	public double doubleValue() {
		return 0;
	}

	@Override
	public float floatValue() {
		return 0;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public long longValue() {
		return 0;
	}

    /**
	 * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
	 **/
	public boolean compareAndSet(BigInteger expect, BigInteger update) {
		boolean didSet = atomicRefValue.compareAndSet(expect, update);
		if (didSet) {
			value = atomicRefValue.get();
			return didSet;
		}else {
			return didSet;
		}
	}
}