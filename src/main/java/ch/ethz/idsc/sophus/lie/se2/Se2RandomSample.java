// code by jph
package ch.ethz.idsc.sophus.lie.se2;

import java.util.Objects;
import java.util.Random;

import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.r2.AngleVector;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;

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
