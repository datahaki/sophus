// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
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
}
