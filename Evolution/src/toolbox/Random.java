package toolbox;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;

import fileHandling.log.Logger;


/**
 * An instance of this class is used to generate a stream of
 * pseudorandom numbers. The class uses a (2**32-1)-bit seed, which is
 * modified using a linear congruential formula. (See Donald Knuth,
 * <i>The Art of Computer Programming, Volume 2</i>, Section 3.2.1.)
 * <p>
 * If two instances of {@code Random} are created with the same
 * seed, and the same sequence of method calls is made for each, they
 * will generate and return identical sequences of numbers.
 **/
public class Random extends java.util.Random{


	private static final long serialVersionUID = -2057624158626154913L;
	private static final File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
    private final AtomicBigInteger seed;
    private static final BigInteger multiplier = BigInteger.valueOf(0x5DEECE66DL);
    private static final BigInteger mask = BigInteger.valueOf((1L << 48) - 1);
    private static final BigInteger addend = BigInteger.valueOf(0xBL);
    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;
    private static final AtomicBigInteger seedUniquifier = new AtomicBigInteger(BigInteger.valueOf(8682522807148012L));
    
    /**
     * This code is equivalent to the parent method of this code it is just here so that i can overcome the private attribute of the variable {@code haveNextNextGaussian}.
     */
    
    public synchronized double nextGaussian() {
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }
	
	private static BigInteger seedUniquifier() {
		//This here is equivalent to the java seed uniquifier code in the Random.java class adapted for BigInteger intakes and manipulation as longs are not sufficient enough for the process.
        for (;;) {
            BigInteger current = seedUniquifier.get(); //Gets the current value of the atomicBigInteger seedUniquifier (@see seedUniquifier) (@see AtomicBigInteger.java, line 29)
            BigInteger next = current.multiply(new BigInteger("1181783497276652981")); //multiply the current value of the atomic long by this number calculated for the best random number generated sequence by mathematics.
            if (seedUniquifier.compareAndSet(current, next)) //if the number is not the same as the last value and the new value is set successfully then return the new value (@see AtomicBigInteger.java, line 89).
                return next;
        }
    }
	
    
	
    public Random() {
    	 //make a new Random Number Generator (constructor) using the bitwise Exclusive Or operation between the seed uniquifier and the system time in nano-seconds which is unlikely to occur twice.
        this(seedUniquifier().xor(new BigInteger(""+System.nanoTime())));
    }
    
    /**
     * Creates a new random number generator using a single {@code BigInteger} seed.
     * The seed is the initial value of the internal state of the pseudorandom
     * number generator which is maintained by method {@link #next}.
     *
     * <p>The invocation {@code new Random(seed)} is equivalent to:
     *  <pre> {@code
     * Random random = new Random();
     * random.setSeed(seed);}</pre>
     *
     * @param seed the initial seed
     * @see   #setSeed(BigInteger)
     **/
    
    public Random(BigInteger seed) {
        if (getClass() == Random.class)
            this.seed = new AtomicBigInteger(initialScramble(seed)); //If the class is random.class (as can be expanded upon by others if not) the scramble the inputted seed in the initial scramble method and set the seed to that value
        else {
            this.seed = new AtomicBigInteger(BigInteger.ZERO); // Set the seed to zero.
            setSeed(seed); //set the seed to the inputted seed using the set seed method
        }
    }
    
    /**
     * Scrambles the initial seed to make the seed even more unlikely to be a duplicate.
     * This is achieved by two logical XOR and AND operations involving the {@code seed}, {@code multiplier} and {@code mask}.
     * <p> the mathematics used is:
     * <pre> {@code
     * 		(seed ^ multiplier) & mask}
     * </pre> 
     *  
     * @param seed the initial seed
     * @return the new scrambled seed, unlikely to be similar to any randomly generated seed
     **/
    
    private static BigInteger initialScramble(BigInteger seed) {
        return (seed.xor(multiplier)).and(mask);
    }
    
    /**
     * <p> Sets the seed of this random number generator using a single
     *  {@code BigInteger} seed. The general contract of  {@code setSeed} is
     * that it alters the state of this random number generator object
     * so as to be in exactly the same state as if it had just been
     * created with the argument seed as a seed. The method
     * {@code setSeed} is implemented by class {@code Random} by
     * atomically updating the seed to
     * {@code (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1)}
     * and clearing the {@code haveNextNextGaussian} flag used by {@code nextGaussian}).
     * 
     * @param seed Initial seed as a BigInteger
     **/
    
    public synchronized void setSeed(BigInteger seed) {
        this.seed.set(initialScramble(seed)); /**set the seed to the scramble of the seed (@see AtomicBigInteger.java. line 20)*/
        haveNextNextGaussian = false; //Set the haveNextNextGaussian flag to false 
    }
    
    /**
     * <p> Generates the next pseudorandom number.
     * The generalisation of what this method does is it returns an integer value
     * and if the argument {@code bits} is between {@code 1} and {@code Integer.MAX_Value - 1} (inclusive)
     * then that many low-order bits of the returned value will be (approximately) independently
     * chosen bit values, each of which is (approximately) equally likely to be {@code 0} or {@code 1}.
     * The method {@code next} is
     * implemented by class {@code Random} by atomically updating the seed to
     *  <pre>{@code (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1)}</pre>
     * and returning
     *  <pre>{@code (int)(seed >>> (Integer.MAX_VALUE - bits))}.</pre>
     *  
     *  @param  bits random bits
     *  @return the next pseudorandom value from this random number generator's sequence.
     **/
    
    protected int next(int bits) {
        BigInteger oldseed, nextseed;
        AtomicBigInteger seed = this.seed;
        do {
            oldseed = seed.get(); //grabs the seed's current value (AtomicBigInteger.java, line 29)
            nextseed = (oldseed.multiply(multiplier).add(addend)).add(mask);
        } while (!seed.compareAndSet(oldseed, nextseed)); //If the new seed is not equivalent to the old seed and gets set successfully then return the new value (AtomicBigInteger.java, line 89)
        return (int)(nextseed.shiftRight((Integer.MAX_VALUE - bits))).intValue(); //Get the integer value of the Bitwise right shift of the nextseed shifted by the increment of ((2^31 - 1)-bits).
    }
    
    /**
     * Returns the next pseudorandom, uniformly distributed {@code BigInteger}
     * value from this random number generator's sequence. The general
     * contract of {@code nextBigInteger} is that one {@code BigInteger} value is
     * pseudorandomly generated and returned.
     *
     * <p>The method {@code nextBigInteger} is implemented by class {@code Random}
     * as if by:
     *  <pre> {@code
     * public BigInteger nextBigInteger() {
     *     return ((BigInteger)next(Integer.MAX_VALUE - 1) << 32) + next(Integer.MAX_VALUE-1);
     * 
     * }}</pre>
     * This will also catch any {@code ArithmeticException} and log it and try again recursively as by:
     * <pre> {@code
     * catch(ArithmeticException e){
     *		try {
     *			StringWriter sw = new StringWriter(); //Create a new string writer
     *			e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
     *			String exceptionAsString = sw.toString(); //Make the string writer write to a string
     *			Logger.main("[UNHEALTHY] Caught Arithmetic Exception at Random generator; stacktrace is:"+exceptionAsString+"\nTrying again", -1, file);
     *			return nextBigInteger();
     *		}}}</pre>
     * If this fails then the code will try and catch any {@code StackOverflowError} and send it to the logger to be reported and crash it:
     * <pre> {@code
     * catch(StackOverflowError e1) {
     *			Logger.StackOverflowErrorHandler(e1,file);
     *		}}
     *</pre>
     * The ground state of this recursive algorithm is 0.
     *
     * @return the next pseudorandom, uniformly distributed {@code BigInteger}
     * value from this random number generator's sequence, or if a {@code StackOverflowError} occurs it will crash automatically and log the report.
     **/
    
    public BigInteger nextBigInteger() {
    	Logger.main("Making new random bigInteger.", 0, file);
    	try {
    		return ((BigInteger.valueOf((next(Integer.MAX_VALUE - 1))))).shiftLeft(32).add(BigInteger.valueOf(next(Integer.MAX_VALUE-1)));
    	}catch(ArithmeticException e){
    		try {
    		StringWriter sw = new StringWriter(); //Create a new string writer
    		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
    		String exceptionAsString = sw.toString(); //Make the string writer write to a string
    		Logger.main("[UNHEALTHY] Caught Arithmetic Exception at Random generator; stacktrace is:"+exceptionAsString+"\nTrying again", -1, file);
    		return nextBigInteger();
    		}catch(StackOverflowError e1) {
    			Logger.StackOverflowErrorHandler(e1,file);
    		}
    	}
    	return BigInteger.ZERO;
    }
	
}
