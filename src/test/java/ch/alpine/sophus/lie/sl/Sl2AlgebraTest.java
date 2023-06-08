// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.LieAlgebraImpl;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class Sl2AlgebraTest {
  private final Distribution distribution = UniformDistribution.of(-0.05, 0.05);

  @Test
  void testBasisA() {
    Distribution local = UniformDistribution.of(-0.3, 0.3);
    Tensor x = RandomVariate.of(local, 3);
    Tensor y = RandomVariate.of(local, 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Sl2Algebra.basis());
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    // System.out.println(Normal.of(matrixAlgebra.ad()));
    assertEquals(ad, matrixAlgebra.ad());
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, 2, 2));
    Tensor mx = matrixAlgebra.toMatrix(x);
    Tensor my = matrixAlgebra.toMatrix(y);
    Tensor gX = Sl2Group.INSTANCE.exp(mx);
    Tensor gY = Sl2Group.INSTANCE.exp(my);
    Tensor gZ = Sl2Group.INSTANCE.log(gX.dot(gY));
    Tensor az = matrixAlgebra.toVector(gZ);
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(matrixAlgebra.ad());
    Tensor rs6 = lieAlgebraImpl.bch(6).apply(x, y);
    Tensor rs8 = lieAlgebraImpl.bch(8).apply(x, y);
    Tensor rsA = lieAlgebraImpl.bch(10).apply(x, y);
    Chop._06.requireClose(az, rs6);
    HsAlgebra hsAlgebra = new HsAlgebra(Sl2Algebra.INSTANCE.ad(), 2, 6);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
    Scalar err1 = Vector2Norm.between(az, rs6);
    Scalar err2 = Vector2Norm.between(az, rs8);
    Scalar err3 = Vector2Norm.between(az, rsA);
    Tensor improve = Tensors.of(err3, err2, err1);
    OrderedQ.require(improve);
  }

  @Test
  void testH() {
    LieAlgebra lieAlgebra = Sl2Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Tensor g = RandomVariate.of(distribution, ad.length());
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    Tensor h = Join.of(Array.zeros(2), RandomVariate.of(distribution, 1));
    BinaryOperator<Tensor> bch = lieAlgebra.bch(6);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Tolerance.CHOP.requireClose(prj_g, prj_gh);
  }

  @Test
  void testHe() {
    LieAlgebra lieAlgebra = Sl2Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Tensor g1 = RandomVariate.of(distribution, ad.length());
    Tensor g2 = RandomVariate.of(distribution, ad.length());
    BinaryOperator<Tensor> bch = lieAlgebra.bch(6);
    Tensor expect = bch.apply(g1, g2);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Sl2Algebra.basis());
    Tensor rotG1 = MatrixExp.of(matrixAlgebra.toMatrix(g1));
    Tensor rotG2 = MatrixExp.of(matrixAlgebra.toMatrix(g2));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG1.dot(rotG2)));
    Chop._10.requireClose(expect, log);
  }

  @Test
  void testHeOnRn() {
    LieAlgebra lieAlgebra = Sl2Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Tensor g = RandomVariate.of(distribution, ad.length());
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Sl2Algebra.basis());
    Tensor rotG = MatrixExp.of(matrixAlgebra.toMatrix(g));
    Tensor rotM = MatrixExp.of(matrixAlgebra.toMatrix(hsAlgebra.lift(m)));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG.dot(rotM)));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
