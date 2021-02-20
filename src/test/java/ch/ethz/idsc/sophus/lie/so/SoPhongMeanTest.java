// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.bm.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.lie.so3.So3Exponential;
import ch.ethz.idsc.sophus.lie.so3.So3Manifold;
import ch.ethz.idsc.sophus.lie.so3.So3Metric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.ArgMax;
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
    Tolerance.CHOP.requireClose(Det.of(m1), RealScalar.ONE);
    {
      Tensor mE0 = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Tolerance.CHOP).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE0);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE0);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE0);
      d0E.add(d1E);
    }
    {
      Tensor mE1 = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Tolerance.CHOP, SoPhongMean.INSTANCE).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE1);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE1);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE1);
      d0E.add(d1E);
    }
  }

  public void testSingle() {
    int n = 1;
    Distribution distribution = UniformDistribution.of(-0.4, 0.4);
    Tensor sequence = Tensors.vector(i -> So3Exponential.INSTANCE.exp(RandomVariate.of(distribution, 3)), n);
    for (Tensor p : sequence) {
      Tolerance.CHOP.requireClose(Det.of(p), RealScalar.ONE);
    }
    Distribution distribution_w = UniformDistribution.of(0.4, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution_w, n));
    Tensor m0 = sequence.get(ArgMax.of(weights));
    OrthogonalMatrixQ.require(m0);
    Tensor m1 = SoPhongMean.INSTANCE.mean(sequence, weights);
    OrthogonalMatrixQ.require(m1);
    Tolerance.CHOP.requireClose(Det.of(m1), RealScalar.ONE);
    // System.out.println(sequence);
    // System.out.println(m1);
    {
      Tensor mE0 = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Tolerance.CHOP).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE0);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE0);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE0);
      Tolerance.CHOP.requireZero(d0E);
      Tolerance.CHOP.requireZero(d1E);
    }
    {
      Tensor mE1 = IterativeBiinvariantMean.of(So3Manifold.INSTANCE, Tolerance.CHOP, SoPhongMean.INSTANCE).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE1);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE1);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE1);
      Tolerance.CHOP.requireZero(d0E);
      Tolerance.CHOP.requireZero(d1E);
    }
  }
}
