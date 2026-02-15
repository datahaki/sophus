// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class TSnMemberQTest {
  @Test
  void test() {
    LinearSubspace linearSubspace = LinearSubspace.of(new TSnMemberQ(Tensors.vector(0, 1, 0))::defect, 3);
    assertEquals(Tensors.fromString("{{1, 0, 0}, {0, 0, 1}}"), linearSubspace.basis());
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 4, 5 })
  void testRandom(int n) {
    Sphere sphereN = new Sphere(n);
    Tensor p = RandomSample.of(sphereN);
    LinearSubspace linearSubspace = LinearSubspace.of(new TSnMemberQ(p)::defect, n + 1);
    TSnMemberQ tSnMemberQ = new TSnMemberQ(p);
    linearSubspace.basis().stream().forEach(tSnMemberQ::require);
    Tensor v = RandomVariate.of(UniformDistribution.unit(20), n + 1);
    Tensor v1 = tSnMemberQ.projection(v);
    Tensor v2 = linearSubspace.projection(v);
    Tolerance.CHOP.requireClose(v1, v2);
  }

  @Test
  void testSerializable() {
    MemberQ memberQ = SnManifold.INSTANCE.isPointQ();
    memberQ.require(UnitVector.of(4, 3));
    TSnMemberQ tSnMemberQ = new TSnMemberQ(UnitVector.of(4, 3));
    tSnMemberQ.require(UnitVector.of(4, 2));
    assertFalse(tSnMemberQ.test(Tensors.vector(1, 2, 3, 4)));
  }

  @Test
  void testSnExpLog() {
    for (int d = 1; d < 4; ++d) {
      RandomSampleInterface randomSampleInterface = new Sphere(d);
      Tensor x = RandomSample.of(randomSampleInterface);
      Tensor y = RandomSample.of(randomSampleInterface);
      Tensor v = new SnExponential(x).log(y);
      TSnMemberQ tSnMemberQ = new TSnMemberQ(x);
      tSnMemberQ.require(v);
      Tensor w = RandomVariate.of(NormalDistribution.standard(), d + 1);
      assertFalse(tSnMemberQ.test(w));
      w = tSnMemberQ.projection(w);
      assertTrue(tSnMemberQ.test(w));
      Tensor w2 = tSnMemberQ.projection(w);
      Tolerance.CHOP.requireClose(w, w2);
    }
  }

  @Disabled
  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> new TSnMemberQ(null));
  }
}
