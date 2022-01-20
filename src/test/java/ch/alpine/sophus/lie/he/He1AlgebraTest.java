// code by jph
package ch.alpine.sophus.lie.he;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class He1AlgebraTest extends TestCase {
  public void testHe1H() {
    LieAlgebra lieAlgebra = He1Algebra.INSTANCE;
    Tensor ad = lieAlgebra.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor h = UnitVector.of(3, 2).multiply(RandomVariate.of(distribution));
    BinaryOperator<Tensor> bch = lieAlgebra.bch(6);
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Tolerance.CHOP.requireClose(prj_g, prj_gh);
  }

  public void testHe1() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g1 = RandomVariate.of(distribution, 3);
    Tensor g2 = RandomVariate.of(distribution, 3);
    BinaryOperator<Tensor> bch = He1Algebra.INSTANCE.bch(6);
    Tensor expect = bch.apply(g1, g2);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(He1Algebra.INSTANCE.basis());
    Tensor rotG1 = MatrixExp.of(matrixAlgebra.toMatrix(g1));
    Tensor rotG2 = MatrixExp.of(matrixAlgebra.toMatrix(g2));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG1.dot(rotG2)));
    Tolerance.CHOP.requireClose(expect, log);
  }

  public void testHe1onR2() {
    Tensor he2 = He1Algebra.INSTANCE.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(he2, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(He1Algebra.INSTANCE.basis());
    Tensor rotG = MatrixExp.of(matrixAlgebra.toMatrix(g));
    Tensor rotM = MatrixExp.of(matrixAlgebra.toMatrix(hsAlgebra.lift(m)));
    Tensor log = matrixAlgebra.toVector(MatrixLog.of(rotG.dot(rotM)));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
