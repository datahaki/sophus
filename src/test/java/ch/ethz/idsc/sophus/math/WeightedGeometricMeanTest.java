// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.GeometricMean;
import junit.framework.TestCase;

public class WeightedGeometricMeanTest extends TestCase {
  public void testSimple() {
    int n = 5;
    Distribution distribution = UniformDistribution.of(0.4, 10);
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor weights = AveragingWeights.of(n);
    Tensor m1 = WeightedGeometricMean.INSTANCE.mean(sequence, weights);
    Tensor m2 = GeometricMean.of(sequence);
    Tolerance.CHOP.requireClose(m1, m2);
  }
}
