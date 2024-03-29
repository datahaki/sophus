// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Random;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class So3AlgebraTest {
  @Test
  void testSimple() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor z = So3Algebra.INSTANCE.bch(6).apply(x, y);
    Chop._08.requireClose(z, res);
  }

  @Test
  void testAlg() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Random random = new Random();
    for (int count = 0; count < 10; ++count) {
      Tensor ga = RandomVariate.of(distribution, random, 3);
      Tensor m1 = RandomVariate.of(distribution, random, 2);
      Tensor m2 = hsAlgebra.action(ga, m1);
      Tensor p = UnitVector.of(3, 2);
      // TODO SOPHUS SO3 investigate where this "change of coordinates" comes from
      SnExponential snExponential = new SnExponential(p);
      Tensor v1 = hsAlgebra.lift(m1); // attach 0
      Tensor p1 = snExponential.exp(v1); // map to S^2
      Tensor g = Tensors.of(ga.Get(1).negate(), ga.Get(0), ga.Get(2));
      Tensor rot = So3Group.INSTANCE.exp(g);
      Tensor p2 = rot.dot(p1); // apply action
      Tensor v2 = snExponential.log(p2);
      Chop._06.requireClose(hsAlgebra.lift(m2), v2);
    }
  }

  @Test
  void testActionH() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Tensor h = Join.of(Array.zeros(2), RandomVariate.of(distribution, 1));
    Tensor m1 = RandomVariate.of(distribution, 2);
    Tensor m2 = hsAlgebra.action(h, m1);
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = So3Group.INSTANCE.exp(h);
    SnExponential snExponential = new SnExponential(p);
    Tensor v1 = hsAlgebra.lift(m1);
    Tensor snm = snExponential.exp(v1);
    Tensor dot = rotation.dot(snm);
    // System.out.println(dot);
    Tensor v2 = snExponential.log(dot);
    Tolerance.CHOP.requireClose(hsAlgebra.lift(m2), v2);
  }

  @Test
  void testSo3H() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor h = UnitVector.of(3, 2).multiply(RandomVariate.of(distribution));
    BinaryOperator<Tensor> bch = So3Algebra.INSTANCE.bch(6);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Chop._10.requireClose(prj_g, prj_gh);
  }

  @Test
  void testSo3S2() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    Tensor rotG = So3Group.INSTANCE.exp(g);
    Tensor rotM = So3Group.INSTANCE.exp(hsAlgebra.lift(m));
    Tensor log = So3Group.INSTANCE.log(rotG.dot(rotM));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
