// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.r2.RotationMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GlnGroupTest extends TestCase {
  public void testSimple() {
    GlnGroupElement element = GlnGroup.INSTANCE.element(RotationMatrix.of(RealScalar.of(0.24)));
    Tensor tensor = element.inverse().combine(RotationMatrix.of(RealScalar.of(0.24)));
    Chop._10.requireClose(tensor, IdentityMatrix.of(2));
  }
}
