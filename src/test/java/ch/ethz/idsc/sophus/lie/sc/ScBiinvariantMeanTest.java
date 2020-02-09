// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.ExponentialDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.GeometricMean;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ScBiinvariantMeanTest extends TestCase {
  public void testSimple() {
    Tensor scalar = ScBiinvariantMean.INSTANCE.mean(Tensors.vector(1, 2, 3).map(Tensors::of), Tensors.fromString("{1/3, 1/3, 1/3}"));
    Chop._10.requireClose(scalar, Tensors.vector(1.8171205928321397));
  }

  public void testGeometricMean() {
    Distribution distribution = ExponentialDistribution.of(1);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n);
      Scalar geomet = GeometricMean.of(sequence).Get();
      Tensor weights = ConstantArray.of(RationalScalar.of(1, sequence.length()), sequence.length());
      Tensor scmean = ScBiinvariantMean.INSTANCE.mean(sequence.map(Tensors::of), weights);
      Chop._10.requireClose(Tensors.of(geomet), scmean);
    }
  }
}
