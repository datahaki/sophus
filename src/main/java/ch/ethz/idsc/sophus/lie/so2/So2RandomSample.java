// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import java.util.Random;

import ch.ethz.idsc.sophus.lie.so.SoRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.r2.RotationMatrix;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;

/** @see SoRandomSample */
public enum So2RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final Distribution DISTRIBUTION = UniformDistribution.of(Pi.VALUE.negate(), Pi.VALUE);

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return RotationMatrix.of(RandomVariate.of(DISTRIBUTION, random));
  }
}
