// code by jph
package ch.alpine.sophus.lie.se;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.mat.IdentityMatrix;

class SeNGroupTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 5 })
  void testMatchCheck(int d) {
    SeNGroup seNGroup = new SeNGroup(d);
    seNGroup.isPointQ().require(IdentityMatrix.of(d + 1));
  }
}
