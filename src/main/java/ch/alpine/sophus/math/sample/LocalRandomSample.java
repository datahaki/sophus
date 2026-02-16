// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class LocalRandomSample implements RandomSampleInterface, Serializable {
  public static RandomSampleInterface of(Exponential exponential, Tensor p, Scalar sigma) {
    return new LocalRandomSample(exponential, p, sigma);
  }

  public static RandomSampleInterface of(Exponential exponential, Tensor p, Number sigma) {
    return new LocalRandomSample(exponential, p, RealScalar.of(sigma));
  }

  // ---
  private final Exponential exponential;
  private final Distribution distribution;
  private final LinearSubspace linearSubspace;
  private final int n;

  private LocalRandomSample(Exponential exponential, Tensor p, Scalar sigma) {
    this.exponential = exponential;
    distribution = NormalDistribution.of(sigma.zero(), sigma);
    linearSubspace = LinearSubspace.of(exponential.isTangentQ()::defect, Dimensions.of(exponential.log(p)));
    n = linearSubspace.dimensions();
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return exponential.exp(linearSubspace.apply(RandomVariate.of(distribution, randomGenerator, n)));
  }
}
