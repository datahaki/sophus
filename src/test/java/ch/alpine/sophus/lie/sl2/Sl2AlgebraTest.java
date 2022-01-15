// code by jph
package ch.alpine.sophus.lie.sl2;

import ch.alpine.sophus.lie.LieAlgebraImpl;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Sl2AlgebraTest extends TestCase {
  public void testBasisA() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.125);
    Tensor y = Tensors.vector(0.15, -0.08, 0.025);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Sl2Algebra.INSTANCE.basis());
    assertEquals(Sl2Algebra.INSTANCE.ad(), matrixAlgebra.ad());
    Tensor mx = matrixAlgebra.toMatrix(x);
    Tensor my = matrixAlgebra.toMatrix(y);
    Tensor gX = Sl2Exponential.INSTANCE.exp(mx);
    Tensor gY = Sl2Exponential.INSTANCE.exp(my);
    Tensor gZ = Sl2Exponential.INSTANCE.log(gX.dot(gY));
    Tensor az = matrixAlgebra.toVector(gZ);
    LieAlgebraImpl lieAlgebraImpl = new LieAlgebraImpl(matrixAlgebra.ad());
    Tensor rs = lieAlgebraImpl.bch(6).apply(x, y);
    Chop._06.requireClose(az, rs);
  }
}
