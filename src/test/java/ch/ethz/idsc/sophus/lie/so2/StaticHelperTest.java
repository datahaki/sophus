// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    StaticHelper.rangeQ(Tensors.vector(1, 2, 3));
    AssertFail.of(() -> StaticHelper.rangeQ(Tensors.vector(1, 2, 7)));
  }
}
