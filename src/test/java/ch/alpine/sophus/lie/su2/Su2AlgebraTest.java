// code by jph
package ch.alpine.sophus.lie.su2;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.LieAlgebraImpl;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.lie.ad.KillingForm;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.mat.ConjugateTranspose;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Trace;
import junit.framework.TestCase;

public class Su2AlgebraTest extends TestCase {
  public void testKillingForm() {
    LieAlgebra lieAlgebra = Su2Algebra.INSTANCE;
    Tensor form = KillingForm.of(lieAlgebra.ad());
    assertEquals(form, DiagonalMatrix.of(-8, -8, -8));
  }

  public void testBch() {
    Distribution local = UniformDistribution.of(-0.2, 0.2);
    Tensor x = RandomVariate.of(local, 3);
    Tensor y = RandomVariate.of(local, 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Su2Algebra.INSTANCE.basis());
    Tensor ad = Su2Algebra.INSTANCE.ad();
    assertEquals(ad, matrixAlgebra.ad());
    Tensor mx = matrixAlgebra.toMatrix(x);
    Tensor my = matrixAlgebra.toMatrix(y);
    Tensor gX = MatrixExp.of(mx);
    Tensor gY = MatrixExp.of(my);
    Tensor gZ = MatrixLog.of(gX.dot(gY));
    Tensor az = matrixAlgebra.toVector(gZ);
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(matrixAlgebra.ad());
    Tensor rs6 = lieAlgebraImpl.bch(6).apply(x, y);
    Tensor rs8 = lieAlgebraImpl.bch(8).apply(x, y);
    Tensor rsA = lieAlgebraImpl.bch(10).apply(x, y);
    Scalar err1 = Vector2Norm.between(az, rs6);
    Scalar err2 = Vector2Norm.between(az, rs8);
    Scalar err3 = Vector2Norm.between(az, rsA);
    Tensor improve = Tensors.of(err3, err2, err1);
    OrderedQ.require(improve);
  }

  public void testBasis() {
    for (Tensor mat : Su2Algebra.INSTANCE.basis()) {
      ExactTensorQ.require(mat);
      assertEquals(ConjugateTranspose.of(mat), mat.negate());
      assertEquals(Trace.of(mat), RealScalar.ZERO);
    }
  }
}
