// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.so.SoGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Matrix2Norm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class StExponentialTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6, 10 })
  void testSimple(int n) {
    for (int k = 1; k <= n; ++k) {
      StiefelManifold stiefelManifold = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(stiefelManifold);
      stiefelManifold.isPointQ().require(p);
      TStMemberQ tStMemberQ = new TStMemberQ(p);
      Tensor v = tStMemberQ.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), k, n));
      Scalar norm = Matrix2Norm.of(v);
      assertFalse(Chop._08.isZero(norm));
      Exponential exponential = stiefelManifold.exponential(p);
      Tensor q = exponential.exp(v);
      assertEquals(Dimensions.of(p), Dimensions.of(q));
      stiefelManifold.isPointQ().require(q);
      Tensor w = exponential.log(q);
      Chop._10.requireClose(v, w);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 8 })
  void testOrthLogMatch(int n) {
    StiefelManifold stiefelManifold = new StiefelManifold(n, n);
    Tensor p = IdentityMatrix.of(n);
    stiefelManifold.isPointQ().require(p);
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    Tensor v = tStMemberQ.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), n, n));
    Exponential exponential = stiefelManifold.exponential(p);
    Tensor q = exponential.exp(v);
    stiefelManifold.isPointQ().require(q);
    Tensor log1 = exponential.log(q);
    Tensor log2 = SoGroup.INSTANCE.exponential(p).log(q);
    Tolerance.CHOP.requireClose(log1, log2);
  }
}
