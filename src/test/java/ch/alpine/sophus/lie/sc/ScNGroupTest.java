package ch.alpine.sophus.lie.sc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.GroupCheck;

class ScNGroupTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 4, 6 })
  void testSeveral(int d) {
    ScNGroup scNGroup = new ScNGroup(d);
    GroupCheck.check(scNGroup);
  }
}
