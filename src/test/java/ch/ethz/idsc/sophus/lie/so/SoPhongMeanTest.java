// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.lie.so3.So3Exponential;
import ch.ethz.idsc.sophus.lie.so3.So3Manifold;
import ch.ethz.idsc.sophus.lie.so3.So3Metric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SoPhongMeanTest extends TestCase {
  public void testSimple() {
    int n = 7;
    Distribution distribution = UniformDistribution.of(-0.4, 0.4);
    Tensor sequence = Tensors.vector(i -> So3Exponential.INSTANCE.exp(RandomVariate.of(distribution, 3)), n);
    Distribution distribution_w = UniformDistribution.of(0.4, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution_w, n));
    Tensor m0 = sequence.get(ArgMax.of(weights));
    OrthogonalMatrixQ.require(m0);
    Tensor m1 = SoPhongMean.INSTANCE.mean(sequence, weights);
    OrthogonalMatrixQ.require(m1);
    BiinvariantMean biinvariantMean = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Chop._08);
    Tensor mE = biinvariantMean.mean(sequence, weights);
    OrthogonalMatrixQ.require(mE);
    Scalar d1E = So3Metric.INSTANCE.distance(m1, mE);
    Scalar d0E = So3Metric.INSTANCE.distance(m0, mE);
    System.out.println(d0E);
    System.out.println(d1E);
  }
}
