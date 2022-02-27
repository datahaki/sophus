// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.lie.so.SoPhongMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
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
