// code by jph
package ch.alpine.sophus.lie.so2;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

/** @see SoRandomSample */
public enum So2RandomSample implements RandomSampleInterface {
  INSTANCE;

  private static final Distribution DISTRIBUTION = UniformDistribution.of(Pi.VALUE.negate(), Pi.VALUE);

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RotationMatrix.of(RandomVariate.of(DISTRIBUTION, randomGenerator));
  }
}
