// code by jph
package ch.alpine.sophus.lie.so3;

import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

public enum So3TestHelper {
  ;
  private static final Random RANDOM = new SecureRandom();
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = So3RandomSample.INSTANCE;

  public static Tensor spawn_So3() {
    return spawn_So3(RANDOM);
  }

  public static Tensor spawn_So3(Random random) {
    return RandomSample.of(RANDOM_SAMPLE_INTERFACE, random);
  }

  public static Tensor spawn_so3() {
    Tensor m = RandomVariate.of(DISTRIBUTION, 3, 3);
    return Transpose.of(m).subtract(m);
  }
}
