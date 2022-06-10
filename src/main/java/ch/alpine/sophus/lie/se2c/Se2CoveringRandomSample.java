// code by jph
package ch.alpine.sophus.lie.se2c;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

// TODO SOPHUS rename&move to more generic
public class Se2CoveringRandomSample implements RandomSampleInterface, Serializable {
  /** @param distribution
   * @return */
  public static RandomSampleInterface uniform(Distribution distribution) {
    return new Se2CoveringRandomSample(distribution, distribution, distribution);
  }

  // ---
  private final Distribution x;
  private final Distribution y;
  private final Distribution a;

  private Se2CoveringRandomSample(Distribution x, Distribution y, Distribution a) {
    this.x = x;
    this.y = y;
    this.a = a;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Tensors.of( //
        RandomVariate.of(x, random), //
        RandomVariate.of(y, random), //
        RandomVariate.of(a, random));
  }
}
