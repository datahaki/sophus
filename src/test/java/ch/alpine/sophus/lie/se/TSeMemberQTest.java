// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.pi.LinearSubspace;

class TSeMemberQTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6 })
  void testDims(int n) {
    int m = n + 1;
    LinearSubspace linearSubspace = LinearSubspace.of(TSeMemberQ.INSTANCE::defect, m, m);
    ExactTensorQ.require(linearSubspace.basis());
    SeNGroup seNGroup = new SeNGroup(n);
    assertEquals(linearSubspace.dimensions(), seNGroup.dimensions());
  }
}
