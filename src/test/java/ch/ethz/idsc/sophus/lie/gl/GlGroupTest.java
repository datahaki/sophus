// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.r2.RotationMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class GlGroupTest extends TestCase {
  public void testSimple() {
    GlGroupElement element = GlGroup.INSTANCE.element(RotationMatrix.of(RealScalar.of(0.24)));
    Tensor tensor = element.inverse().combine(RotationMatrix.of(RealScalar.of(0.24)));
    Tolerance.CHOP.requireClose(tensor, IdentityMatrix.of(2));
  }
}
