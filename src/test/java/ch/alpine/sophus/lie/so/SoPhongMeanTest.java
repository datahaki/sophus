// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.lie.so3.So3Group;
import ch.alpine.sophus.lie.so3.So3Metric;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class SoPhongMeanTest {
  @Test
  public void testSimple() {
    int n = 7;
    Distribution distribution = UniformDistribution.of(-0.4, 0.4);
    Tensor sequence = Tensors.vector(i -> So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3)), n);
    Distribution distribution_w = UniformDistribution.of(0.4, 1);
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution_w, n));
    Tensor m0 = sequence.get(ArgMax.of(weights));
    OrthogonalMatrixQ.require(m0);
    Tensor m1 = SoPhongMean.INSTANCE.mean(sequence, weights);
    OrthogonalMatrixQ.require(m1);
    Tolerance.CHOP.requireClose(Det.of(m1), RealScalar.ONE);
    {
      Tensor mE0 = IterativeBiinvariantMean.argmax(So3Group.INSTANCE, Tolerance.CHOP).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE0);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE0);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE0);
      d0E.add(d1E);
    }
    {
      Tensor mE1 = IterativeBiinvariantMean.of(So3Group.INSTANCE, Tolerance.CHOP, SoPhongMean.INSTANCE).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE1);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE1);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE1);
      d0E.add(d1E);
    }
  }

  @Test
  public void testSingle() {
    int n = 1;
    Distribution distribution = UniformDistribution.of(-0.4, 0.4);
    Tensor sequence = Tensors.vector(i -> So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3)), n);
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
      Tensor mE0 = IterativeBiinvariantMean.argmax(So3Group.INSTANCE, Tolerance.CHOP).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE0);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE0);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE0);
      Tolerance.CHOP.requireZero(d0E);
      Tolerance.CHOP.requireZero(d1E);
    }
    {
      Tensor mE1 = IterativeBiinvariantMean.of(So3Group.INSTANCE, Tolerance.CHOP, SoPhongMean.INSTANCE).mean(sequence, weights);
      OrthogonalMatrixQ.require(mE1);
      Scalar d0E = So3Metric.INSTANCE.distance(m0, mE1);
      Scalar d1E = So3Metric.INSTANCE.distance(m1, mE1);
      Tolerance.CHOP.requireZero(d0E);
      Tolerance.CHOP.requireZero(d1E);
    }
  }

  @Test
  public void testTwoExactMidpoint() {
    Distribution distribution = UniformDistribution.of(-0.2, 0.2);
    Tensor p = So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3));
    Tensor q = So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3));
    Tensor m1 = So3Group.INSTANCE.midpoint(p, q);
    Tensor m2 = SoPhongMean.INSTANCE.mean(Tensors.of(p, q), Tensors.vector(0.5, 0.5));
    Tolerance.CHOP.requireClose(m1, m2);
  }

  @Test
  public void testTwoMidpoint() {
    Distribution distribution = UniformDistribution.of(-0.2, 0.2);
    Tensor p = So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3));
    Tensor q = So3Group.INSTANCE.exp(RandomVariate.of(distribution, 3));
    Tensor weights = Tensors.vector(0.2, 0.8);
    AffineQ.require(weights);
    Tensor sequence = Tensors.of(p, q);
    Tensor m1 = So3Group.INSTANCE.biinvariantMean(Tolerance.CHOP).mean(sequence, weights);
    Tensor m2 = SoPhongMean.INSTANCE.mean(sequence, weights);
    assertTrue(Scalars.lessThan(FrobeniusNorm.between(m1, m2), RealScalar.of(0.1)));
    // System.out.println(Pretty.of(m1));
    // System.out.println(Pretty.of(m2));
  }
}
