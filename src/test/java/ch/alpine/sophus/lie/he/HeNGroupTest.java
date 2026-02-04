package ch.alpine.sophus.lie.he;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.GroupCheck;
import ch.alpine.sophus.lie.LieGroup;

class HeNGroupTest {
  @Test
  void test() {
    LieGroup lieGroup = new HeNGroup(2);
    GroupCheck.check(lieGroup);
  }
}
