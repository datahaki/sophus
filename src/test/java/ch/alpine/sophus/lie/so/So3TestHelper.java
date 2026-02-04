// code by jph
package ch.alpine.sophus.lie.so;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

public enum So3TestHelper {
  ;
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(10));
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = So3Group.INSTANCE;

  public static Tensor spawn_So3() {
    return spawn_So3(ThreadLocalRandom.current());
  }

  public static Tensor spawn_So3(RandomGenerator randomGenerator) {
    return RandomSample.of(RANDOM_SAMPLE_INTERFACE, randomGenerator);
  }

  public static Tensor spawn_so3() {
    Tensor m = RandomVariate.of(DISTRIBUTION, 3, 3);
    return Transpose.of(m).subtract(m);
  }
}
