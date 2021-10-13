// code by jph
package ch.alpine.sophus.hs.r2;

import java.io.IOException;

import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.sophus.math.Bijection;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2BijectionTest extends TestCase {
  public void testSimple() {
    Bijection bijection = new Se2Bijection(Tensors.vector(2, 3, .3));
    Tensor vector = Tensors.vector(0.32, -0.98);
    Tensor sameor = bijection.inverse().apply(bijection.forward().apply(vector));
    Chop._14.requireClose(vector, sameor);
  }

  public void testInverse() {
    Se2Bijection se2Bijection = new Se2Bijection(Tensors.vector(2, -3, 1.3));
    Tensor matrix = se2Bijection.forward_se2();
    Tensor se2inv = Inverse.of(matrix);
    Tensor xya = Se2Matrix.toVector(se2inv);
    Se2Bijection se2Inverse = new Se2Bijection(xya);
    Chop._14.requireClose(se2Inverse.forward_se2().dot(matrix), IdentityMatrix.of(3));
    Tensor vector = Tensors.vector(5, 6);
    Tensor imaged = se2Bijection.forward().apply(vector);
    Chop._14.requireClose(se2Inverse.forward().apply(imaged), vector);
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    Se2Bijection se2Bijection = new Se2Bijection(Tensors.vector(2, -3, 1.3));
    Se2Bijection copy = Serialization.copy(se2Bijection);
    Tensor vector = Tensors.vector(0.32, -0.98);
    assertEquals(se2Bijection.forward().apply(vector), copy.forward().apply(vector));
  }
}
