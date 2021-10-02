// code by jph
package ch.alpine.sophus.math.sample;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;

/** RandomSample is a generalization of {@link RandomVariate}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/RandomSample.html">RandomSample</a> */
public enum RandomSample {
  ;
  private static final Random RANDOM = new SecureRandom();

  /** @param randomSampleInterface
   * @param random
   * @return */
  public static Tensor of(RandomSampleInterface randomSampleInterface, Random random) {
    return randomSampleInterface.randomSample(random);
  }

  /** @param randomSampleInterface
   * @return single random sample generated by given randomSampleInterface */
  public static Tensor of(RandomSampleInterface randomSampleInterface) {
    return randomSampleInterface.randomSample(RANDOM);
  }

  /** @param randomSampleInterface
   * @param random
   * @param length non-negative
   * @return
   * @throws Exception if given length is negative */
  public static Tensor of(RandomSampleInterface randomSampleInterface, Random random, int length) {
    return Tensor.of(Stream.generate(() -> randomSampleInterface.randomSample(random)).limit(length));
  }

  /** @param randomSampleInterface
   * @param length non-negative
   * @return length number of random samples generated by given randomSampleInterface */
  public static Tensor of(RandomSampleInterface randomSampleInterface, int length) {
    return of(randomSampleInterface, RANDOM, length);
  }
}
