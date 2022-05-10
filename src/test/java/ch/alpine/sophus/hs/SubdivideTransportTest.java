// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so3.So3Manifold;

class SubdivideTransportTest {
  @Test
  public void testSubdivideFail() {
    assertThrows(Exception.class, () -> SubdivideTransport.of( //
        new PoleLadder(So3Manifold.INSTANCE), So3Manifold.INSTANCE, 0));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> SubdivideTransport.of(null, So3Manifold.INSTANCE, 2));
    assertThrows(Exception.class, () -> SubdivideTransport.of(new PoleLadder(So3Manifold.INSTANCE), null, 2));
  }
}
