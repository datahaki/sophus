// code by jph
package ch.alpine.sophus.hs.h;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class LBilinearFormTest {
  @Test
  void testVisibility() {
    assertFalse(Modifier.isPublic(LBilinearForm.class.getModifiers()));
  }
}
