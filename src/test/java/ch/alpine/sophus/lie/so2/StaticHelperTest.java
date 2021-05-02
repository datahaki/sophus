// code by jph
package ch.alpine.sophus.lie.so2;

import java.lang.reflect.Modifier;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    StaticHelper.rangeQ(Tensors.vector(1, 2, 3));
    AssertFail.of(() -> StaticHelper.rangeQ(Tensors.vector(1, 2, 7)));
  }

  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
