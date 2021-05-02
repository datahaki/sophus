// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.Random;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.math.WeightedGeometricMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.red.ArgMax;
import ch.alpine.tensor.red.GeometricMean;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdPhongMeanTest extends TestCase {
  public void testSimple() {
    Random random = new Random();
    for (int d = 2; d < 4; ++d) {
      int n = d * (d + 1) / 2 + 1 + random.nextInt(3);
      int fd = d;
      Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fd), n);
      Distribution distribution = UniformDistribution.of(0.1, 1);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
      Tensor m0 = sequence.get(ArgMax.of(weights));
      Tensor m1 = SpdPhongMean.INSTANCE.mean(sequence, weights);
      BiinvariantMean biinvariantMean = IterativeBiinvariantMean.of(SpdManifold.INSTANCE, Chop._10);
      Tensor mE0 = biinvariantMean.mean(sequence, weights);
      Scalar d0 = SpdMetric.INSTANCE.distance(m0, mE0);
      Scalar d1 = SpdMetric.INSTANCE.distance(m1, mE0);
      assertTrue(Scalars.lessThan(d1, d0));
    }
  }

  public void testMidpoint() {
    int n = 2;
    Tensor p = TestHelper.generateSpd(n);
    Tensor q = TestHelper.generateSpd(n);
    Tensor m1 = SpdGeodesic.INSTANCE.midpoint(p, q);
    SpdMemberQ.INSTANCE.require(m1);
    Tensor m2 = SpdPhongMean.INSTANCE.mean(Tensors.of(p, q), Tensors.vector(0.5, 0.5));
    SpdMemberQ.INSTANCE.require(m2);
    Tensor m3 = GeometricMean.of(Unprotect.byRef(p, q));
    // SpdMemberQ.INSTANCE.require(m3);
    // System.out.println(Pretty.of(m1.map(Round._3)));
    // System.out.println(Pretty.of(m2.map(Round._3)));
    m1.add(m3);
  }

  public void testMidpointDiagonal() {
    Tensor p = DiagonalMatrix.of(2, 0.7);
    SpdMemberQ.INSTANCE.require(p);
    Tensor q = DiagonalMatrix.of(3, 0.1);
    SpdMemberQ.INSTANCE.require(q);
    Tensor m = SpdManifold.INSTANCE.midpoint(p, q);
    Tensor s = GeometricMean.of(Unprotect.byRef(p, q));
    Tolerance.CHOP.requireClose(m, s);
  }

  public void testWeightedDiagonal() {
    Tensor p = DiagonalMatrix.of(2, 0.7);
    SpdMemberQ.INSTANCE.require(p);
    Tensor q = DiagonalMatrix.of(3, 0.1);
    SpdMemberQ.INSTANCE.require(q);
    Tensor weights = Tensors.vector(0.4, 0.6);
    Tensor sequence = Unprotect.byRef(p, q);
    Tensor m1 = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
    // Tensor m1 = SpdManifold.INSTANCE.midpoint(p, q);
    Tensor m2 = WeightedGeometricMean.INSTANCE.mean(sequence, weights);
    Tolerance.CHOP.requireClose(m1, m2);
    // System.out.println(Pretty.of(m1.map(Round._3)));
    // System.out.println(Pretty.of(m2.map(Round._3)));
  }
}