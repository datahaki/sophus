// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.lie.so.SoPhongMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.OrderedQ;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.ArgMax;
import junit.framework.TestCase;

public class So3FastMeanTest extends TestCase {
  public void testSimple() {
    int n = 7;
    Distribution distribution = UniformDistribution.of(-0.2, 0.2);
    Tensor sequence = Tensors.vector(i -> So3Exponential.INSTANCE.exp(RandomVariate.of(distribution, 3)), n);
    Distribution distribution_w = UniformDistribution.of(0.4, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution_w, n));
    Tensor m0 = sequence.get(ArgMax.of(weights));
    OrthogonalMatrixQ.require(m0);
    Tensor m1 = SoPhongMean.INSTANCE.mean(sequence, weights);
    OrthogonalMatrixQ.require(m1);
    Tensor m2 = So3FastMean.INSTANCE.mean(sequence, weights);
    Tensor mE = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Tolerance.CHOP).mean(sequence, weights);
    Tensor distances = Tensors.of( //
        So3Metric.INSTANCE.distance(mE, m2), //
        So3Metric.INSTANCE.distance(mE, m1), //
        So3Metric.INSTANCE.distance(mE, m0));
    OrderedQ.require(distances);
  }
}
