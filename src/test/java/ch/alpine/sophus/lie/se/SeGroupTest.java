// code by jph
package ch.alpine.sophus.lie.se;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.mat.IdentityMatrix;

class SeGroupTest {
  @Test
  void test() {
    SeGroup.INSTANCE.requireMember(IdentityMatrix.of(2));
    SeGroup.INSTANCE.requireMember(IdentityMatrix.of(3));
    SeGroup.INSTANCE.requireMember(IdentityMatrix.of(4));
  }
}
