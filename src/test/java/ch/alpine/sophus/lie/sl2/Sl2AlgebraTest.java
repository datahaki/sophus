// code by jph
package ch.alpine.sophus.lie.sl2;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.LieAlgebraImpl;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.lie.ad.KillingForm;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Sl2AlgebraTest extends TestCase {
  private final Distribution distribution = UniformDistribution.of(-0.05, 0.05);

  public void testBasisA() {
    Tensor x = RandomVariate.of(distribution, 3);
    Tensor y = RandomVariate.of(distribution, 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Sl2Algebra.INSTANCE.basis());
    Tensor ad = Sl2Algebra.INSTANCE.ad();
    // System.out.println(Normal.of(matrixAlgebra.ad()));
    assertEquals(ad, matrixAlgebra.ad());
    Tensor form = KillingForm.of(ad);
    assertEquals(form, DiagonalMatrix.of(-2, 2, 2));
    Tensor mx = matrixAlgebra.toMatrix(x);
    Tensor my = matrixAlgebra.toMatrix(y);
    Tensor gX = Sl2Exponential.INSTANCE.exp(mx);
    Tensor gY = Sl2Exponential.INSTANCE.exp(my);
    Tensor gZ = Sl2Exponential.INSTANCE.log(gX.dot(gY));
    Tensor az = matrixAlgebra.toVector(gZ);
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(matrixAlgebra.ad());
    Tensor rs = lieAlgebraImpl.bch(6).apply(x, y);
    Chop._06.requireClose(az, rs);
    HsAlgebra hsAlgebra = new HsAlgebra(Sl2Algebra.INSTANCE.ad(), 2, 6);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
  }

  public void testH() {
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

  public void testHe() {
    LieAlgebra lieAlgebra = Sl2Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Tensor g1 = RandomVariate.of(distribution, ad.length());
    Tensor g2 = RandomVariate.of(distribution, ad.length());
    BinaryOperator<Tensor> bch = lieAlgebra.bch(6);
    Tensor expect = bch.apply(g1, g2);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(lieAlgebra.basis());
    Tensor rotG1 = MatrixExp.of(matrixAlgebra.toMatrix(g1));
    Tensor rotG2 = MatrixExp.of(matrixAlgebra.toMatrix(g2));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG1.dot(rotG2)));
    Tolerance.CHOP.requireClose(expect, log);
  }

  public void testHeOnRn() {
    LieAlgebra lieAlgebra = Sl2Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Tensor g = RandomVariate.of(distribution, ad.length());
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(lieAlgebra.basis());
    Tensor rotG = MatrixExp.of(matrixAlgebra.toMatrix(g));
    Tensor rotM = MatrixExp.of(matrixAlgebra.toMatrix(hsAlgebra.lift(m)));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG.dot(rotM)));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
