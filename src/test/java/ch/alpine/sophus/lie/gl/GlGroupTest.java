// code by jph
package ch.alpine.sophus.lie.gl;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;

class GlGroupTest {
  @Test
  public void testSimple() {
    GlGroupElement element = GlGroup.INSTANCE.element(RotationMatrix.of(RealScalar.of(0.24)));
    Tensor tensor = element.inverse().combine(RotationMatrix.of(RealScalar.of(0.24)));
    Tolerance.CHOP.requireClose(tensor, IdentityMatrix.of(2));
  }
}
