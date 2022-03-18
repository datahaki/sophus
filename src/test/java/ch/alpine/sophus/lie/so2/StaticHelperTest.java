// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;

public class StaticHelperTest {
  @Test
  public void testSimple() {
    StaticHelper.rangeQ(Tensors.vector(1, 2, 3));
    AssertFail.of(() -> StaticHelper.rangeQ(Tensors.vector(1, 2, 7)));
  }

  @Test
  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
