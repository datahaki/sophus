// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Random;

import ch.alpine.sophus.bm.BchBiinvariantMean;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.sn.SnBiinvariantMean;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3AlgebraTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor z = So3Algebra.INSTANCE.bch(6).apply(x, y);
    Chop._08.requireClose(z, res);
  }

  public void testAlg() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Random random = new Random();
    for (int count = 0; count < 10; ++count) {
      Tensor ga = RandomVariate.of(distribution, random, 3);
      Tensor m1 = RandomVariate.of(distribution, random, 2);
      Tensor m2 = hsAlgebra.action(ga, m1);
      Tensor p = UnitVector.of(3, 2);
      // TODO investigate where this "change of coordinates" comes from
      SnExponential snExponential = new SnExponential(p);
      Tensor v1 = hsAlgebra.lift(m1); // attach 0
      Tensor p1 = snExponential.exp(v1); // map to S^2
      Tensor g = Tensors.of(ga.Get(1).negate(), ga.Get(0), ga.Get(2));
      Tensor rot = So3Exponential.INSTANCE.exp(g);
      Tensor p2 = rot.dot(p1); // apply action
      Tensor v2 = snExponential.log(p2);
      Chop._06.requireClose(hsAlgebra.lift(m2), v2);
    }
  }

  public void testActionH() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Tensor h = Join.of(Array.zeros(2), RandomVariate.of(distribution, 1));
    Tensor m1 = RandomVariate.of(distribution, 2);
    Tensor m2 = hsAlgebra.action(h, m1);
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = So3Exponential.INSTANCE.exp(h);
    SnExponential snExponential = new SnExponential(p);
    Tensor v1 = hsAlgebra.lift(m1);
    Tensor snm = snExponential.exp(v1);
    Tensor dot = rotation.dot(snm);
    // System.out.println(dot);
    Tensor v2 = snExponential.log(dot);
    Tolerance.CHOP.requireClose(hsAlgebra.lift(m2), v2);
  }

  public void testMean() {
    Distribution distributionX = UniformDistribution.of(-0.1, 0.1);
    Distribution distributionW = UniformDistribution.of(0.2, 1);
    LieAlgebra lieAlgebra = So3Algebra.INSTANCE;
    HsAlgebra hsAlgebra = new HsAlgebra(lieAlgebra.ad(), 2, 8);
    Random random = new Random();
    for (int n = 3; n < 7; ++n) {
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributionW, random, n));
      Tensor sequence_m = RandomVariate.of(distributionX, random, n, 2);
      Tensor sequence_g = Tensor.of(sequence_m.stream().map(hsAlgebra::lift));
      BiinvariantMean biinvariantMean = BchBiinvariantMean.of(lieAlgebra.bch(8), Tolerance.CHOP);
      Tensor m_avg = hsAlgebra.projection(biinvariantMean.mean(sequence_g, weights));
      SnExponential snExponential = new SnExponential(UnitVector.of(3, 2));
      Tensor pointsS2 = Tensor.of(sequence_g.stream().map(snExponential::exp));
      Tensor meanS2 = SnBiinvariantMean.INSTANCE.mean(pointsS2, weights);
      Tensor res = snExponential.log(meanS2).extract(0, 2);
      System.out.println(m_avg);
      System.out.println(res);
      Chop._04.requireClose(m_avg, res);
      System.out.println("---");
    }
  }
}
