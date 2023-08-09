// code by jph
package ch.alpine.sophus.math;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Cycles;

class PermutationTest {
  @Test
  void test() {
    Cycles cycles = Cycles.of(Tensors.fromString("{{1,5,7},{2,3}}"));
    Permutation permutation = new Permutation(cycles);
    permutation.combine(Tensors.fromString("{{3,5},{1,2}}"));
  }
}
