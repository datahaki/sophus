// code by jph
package ch.alpine.sophus.lie.sl2;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class Sl2AlgebraTest extends TestCase {
  public void testSe2ExpExpLog() {
    Exponential exponential = Sl2Exponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    // Tensor mX = exponential.exp(x);
    // Tensor mY = exponential.exp(y);
    // System.out.println(mX);
    // System.out.println(mY);
    // Tensor res = exponential.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    // Tensor z = Se2Algebra.INSTANCE.bch(6).apply(x, y);
    // Chop._06.requireClose(z, res);
  }
}
