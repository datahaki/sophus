// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;

class StiefelManifoldTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 500 })
  void testSphere1(int n) {
    StiefelManifold stiefelManifold = new StiefelManifold(n, 1);
    Tensor p = Tensors.of(UnitVector.of(n, 0));
    stiefelManifold.isPointQ().require(p);
    Exponential exponential = stiefelManifold.exponential(p);
    Tensor v = Tensors.of(UnitVector.of(n, 1));
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    tStMemberQ.require(v);
    Tolerance.CHOP.requireClose(tStMemberQ.projection(v), v);
    Tensor q = exponential.exp(v);
    Tolerance.CHOP.requireClose(q.get(0).extract(0, 2), AngleVector.of(RealScalar.ONE));
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 500 })
  void testSphere2(int n) {
    StiefelManifold stiefelManifold = new StiefelManifold(n, 1);
    Tensor p = Tensors.of(UnitVector.of(n, 0));
    stiefelManifold.isPointQ().require(p);
    Exponential exponential = stiefelManifold.exponential(p);
    Tensor v = Tensors.of(UnitVector.of(n, 2));
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    tStMemberQ.require(v);
    Tolerance.CHOP.requireClose(tStMemberQ.projection(v), v);
    Tensor q = exponential.exp(v);
    Tolerance.CHOP.requireClose(Tensors.of(q.get(0, 0), q.get(0, 2)), AngleVector.of(RealScalar.ONE));
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 8 })
  void testOrth(int n) {
    StiefelManifold stiefelManifold = new StiefelManifold(n, n);
    Tensor p = IdentityMatrix.of(n);
    stiefelManifold.isPointQ().require(p);
    Exponential exponential = stiefelManifold.exponential(p);
    Tensor vv = RandomVariate.of(ExponentialDistribution.standard(), n, n);
    Tensor v = Transpose.of(vv).subtract(vv);
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    tStMemberQ.require(v);
    Tolerance.CHOP.requireClose(tStMemberQ.projection(v), v);
    Tensor q1 = exponential.exp(v);
    Tensor q2 = SoGroup.INSTANCE.exponential(p).exp(v);
    Tolerance.CHOP.requireClose(q1, q2);
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 8 })
  void testSpecialOrth(int n) {
    StiefelManifold stiefelManifold = new StiefelManifold(n, n - 1);
    Tensor p = Drop.tail(IdentityMatrix.of(n), 1);
    stiefelManifold.isPointQ().require(p);
    Exponential exponential = stiefelManifold.exponential(p);
    Tensor vv = RandomVariate.of(ExponentialDistribution.standard(), n - 1, n);
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    Tensor v = tStMemberQ.projection(vv);
    tStMemberQ.require(v);
    Tensor q1 = exponential.exp(v);
    stiefelManifold.isPointQ().require(q1);
    // Tensor q2 = SoGroup.INSTANCE.exponential(p).exp(v);
    // Tolerance.CHOP.requireClose(q1, q2);
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void testSimple(int n) {
    for (int k = n - 2; k <= n; ++k) {
      RandomSampleInterface randomSampleInterface = new StiefelManifold(n, k);
      Tensor matrix = RandomSample.of(randomSampleInterface);
      StManifold.INSTANCE.isPointQ().require(matrix);
    }
  }

  @Test
  void testMember() {
    StiefelManifold stRandomSample = new StiefelManifold(5, 3);
    Tensor matrix = RandomSample.of(stRandomSample);
    assertEquals(Dimensions.of(matrix), List.of(3, 5));
    // StMemberQ.INSTANCE.require(matrix);
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new StiefelManifold(3, -1));
    assertThrows(Exception.class, () -> new StiefelManifold(3, 4));
    assertThrows(Exception.class, () -> new StiefelManifold(-3, -4));
  }
}
