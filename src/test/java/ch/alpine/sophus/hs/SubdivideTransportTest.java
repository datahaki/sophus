// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so.So3Group;

class SubdivideTransportTest {
  @Test
  void testSubdivideFail() {
    assertThrows(Exception.class, () -> SubdivideTransport.of( //
        new PoleLadder(So3Group.INSTANCE), So3Group.INSTANCE, 0));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> SubdivideTransport.of(null, So3Group.INSTANCE, 2));
    assertThrows(Exception.class, () -> SubdivideTransport.of(new PoleLadder(So3Group.INSTANCE), null, 2));
  }
}
