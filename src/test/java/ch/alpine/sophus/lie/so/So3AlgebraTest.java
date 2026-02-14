// code by jph
package ch.alpine.sophus.lie.so;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.hs.s.SnExponential;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
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
    Tensor mX = So3Exponential.vectorExp(x);
    Tensor mY = So3Exponential.vectorExp(y);
    Tensor res = So3Exponential.vector_log(mX.dot(mY));
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    TensorBinaryOperator tbo = BakerCampbellHausdorff.of(matrixAlgebra.ad(), 6);
    Tensor z = tbo.apply(x, y);
    Chop._08.requireClose(z, res);
  }

  @Disabled
  @Test
  void testAlg() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    for (int count = 0; count < 10; ++count) {
      Tensor ga = RandomVariate.of(distribution, 3);
      Tensor m1 = RandomVariate.of(distribution, 2);
      Tensor m2 = hsAlgebra.action(ga, m1);
      Tensor p = UnitVector.of(3, 2);
      // TODO SOPHUS SO3 investigate where this "change of coordinates" comes from
      SnExponential snExponential = new SnExponential(p);
      Tensor v1 = hsAlgebra.lift(m1); // attach 0
      Tensor p1 = snExponential.exp(v1); // map to S^2
      Tensor g = Tensors.of(ga.Get(1).negate(), ga.Get(0), ga.Get(2));
      Tensor rot = So3Group.INSTANCE.exponential0().exp(g);
      Tensor p2 = rot.dot(p1); // apply action
      Tensor v2 = snExponential.log(p2);
      Chop._06.requireClose(hsAlgebra.lift(m2), v2);
    }
  }

  @Disabled
  @Test
  void testActionH() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    Tensor h = Join.of(Array.zeros(2), RandomVariate.of(distribution, 1));
    Tensor m1 = RandomVariate.of(distribution, 2);
    Tensor m2 = hsAlgebra.action(h, m1);
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = So3Group.INSTANCE.exponential0().exp(h);
    SnExponential snExponential = new SnExponential(p);
    Tensor v1 = hsAlgebra.lift(m1);
    Tensor snm = snExponential.exp(v1);
    Tensor dot = rotation.dot(snm);
    Tensor v2 = snExponential.log(dot);
    Tolerance.CHOP.requireClose(hsAlgebra.lift(m2), v2);
  }

  @Test
  void testSo3H() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    Tensor so3 = matrixAlgebra.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor h = UnitVector.of(3, 2).multiply(RandomVariate.of(distribution));
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(so3, 6);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Chop._10.requireClose(prj_g, prj_gh);
  }

  @Disabled
  @Test
  void testSo3S2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    Tensor so3 = matrixAlgebra.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    Tensor rotG = So3Group.INSTANCE.exponential0().exp(g);
    Tensor rotM = So3Group.INSTANCE.exponential0().exp(hsAlgebra.lift(m));
    Tensor log = So3Group.INSTANCE.exponential0().log(rotG.dot(rotM));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
