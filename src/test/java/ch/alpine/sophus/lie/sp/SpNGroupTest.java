// code by jph
package ch.alpine.sophus.lie.sp;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomSample;

class SpNGroupTest {
  @Test
  void test() {
    SpNGroup spNGroup = new SpNGroup(3);
    Tensor p = RandomSample.of(spNGroup);
    spNGroup.isPointQ().require(p);
  }
}
