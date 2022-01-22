// code by jph
package ch.alpine.sophus.lie.he;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class HeAlgebraTest extends TestCase {
  private final Distribution distribution = UniformDistribution.of(-0.05, 0.05);

  public void testHeH() {
    for (int n = 1; n <= 3; ++n) {
      LieAlgebra lieAlgebra = new HeAlgebra(n);
      Tensor ad = lieAlgebra.ad();
      Tensor g = RandomVariate.of(distribution, ad.length());
      HsAlgebra hsAlgebra = new HsAlgebra(ad, n + 1, 8);
      Tensor h = Join.of(Array.zeros(n + 1), RandomVariate.of(distribution, n));
      BinaryOperator<Tensor> bch = lieAlgebra.bch(6);
      Tensor prj_g = hsAlgebra.projection(g);
      Tensor res = bch.apply(g, h);
      Tensor prj_gh = hsAlgebra.projection(res);
      Tolerance.CHOP.requireClose(prj_g, prj_gh);
    }
  }

  public void testHe() {
    for (int n = 1; n <= 3; ++n) {
      LieAlgebra lieAlgebra = new HeAlgebra(n);
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
  }

  public void testHeOnRn() {
    for (int n = 1; n <= 3; ++n) {
      LieAlgebra lieAlgebra = new HeAlgebra(n);
      Tensor ad = lieAlgebra.ad();
      Tensor g = RandomVariate.of(distribution, ad.length());
      Tensor m = RandomVariate.of(distribution, n + 1);
      HsAlgebra hsAlgebra = new HsAlgebra(ad, n + 1, 8);
      Tensor expect = hsAlgebra.action(g, m);
      MatrixAlgebra matrixAlgebra = new MatrixAlgebra(lieAlgebra.basis());
      Tensor rotG = MatrixExp.of(matrixAlgebra.toMatrix(g));
      Tensor rotM = MatrixExp.of(matrixAlgebra.toMatrix(hsAlgebra.lift(m)));
      Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG.dot(rotM)));
      Tensor prj = hsAlgebra.projection(log);
      Tolerance.CHOP.requireClose(expect, prj);
    }
  }

  public void testZeroFail() {
    AssertFail.of(() -> new HeAlgebra(0));
  }
}