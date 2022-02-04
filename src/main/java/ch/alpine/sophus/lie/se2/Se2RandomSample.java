// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.Objects;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

public class Se2RandomSample implements RandomSampleInterface {
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Pi.VALUE.negate(), Pi.VALUE);

  /** @param distribution of radius from origin regarding (x, y) coordinates
   * @return */
  public static RandomSampleInterface of(Distribution distribution) {
    return new Se2RandomSample(Objects.requireNonNull(distribution));
  }

  // ---
  private final Distribution distribution;

  private Se2RandomSample(Distribution distribution) {
    this.distribution = distribution;
  }

  @Override
  public Tensor randomSample(Random random) {
    Scalar angle = RandomVariate.of(DISTRIBUTION, random);
    return AngleVector.of(angle).multiply(RandomVariate.of(distribution, random)) //
        .append(RandomVariate.of(DISTRIBUTION, random));
  }
}
