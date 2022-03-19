// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;

public class StaticHelperTest {
  @Test
  public void testSimple() {
    StaticHelper.rangeQ(Tensors.vector(1, 2, 3));
    assertThrows(Exception.class, () -> StaticHelper.rangeQ(Tensors.vector(1, 2, 7)));
  }

  @Test
  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
