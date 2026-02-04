package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.gr.GrManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class TStMemberQTest {
  @Test
  void test() {
    int n = 7;
    int k = 3;
    StiefelManifold stiefelManifold = new StiefelManifold(n, k);
    Tensor p = RandomSample.of(stiefelManifold);
    TStMemberQ tStMemberQ = new TStMemberQ(p);
    LinearSubspace linearSubspace = LinearSubspace.of(tStMemberQ::defect, k, n);
    int dim = n * k - k * (k + 1) / 2;
    assertEquals(linearSubspace.basis().length(), dim);
    for (Tensor v : linearSubspace.basis()) {
      Tensor q = stiefelManifold.exponential(p).exp(v);
      stiefelManifold.requireMember(q);
    }
    int bl = linearSubspace.basis().length();
    Tensor weights = RandomVariate.of(NormalDistribution.standard(), bl);
    Tensor v = weights.dot(linearSubspace.basis());
    tStMemberQ.requireMember(v);
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void testSimple(int n) {
    for (int k = n - 2; k <= n; ++k) {
      RandomSampleInterface randomSampleInterface = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      StManifold.INSTANCE.requireMember(p);
      final Tensor c = RandomVariate.of(NormalDistribution.standard(), k, n);
      TStMemberQ tStMemberQ = new TStMemberQ(p);
      Tensor v = tStMemberQ.projection(c);
      assertEquals(Dimensions.of(p), Dimensions.of(v));
      assertTrue(tStMemberQ.isMember(v));
      Tensor v2 = tStMemberQ.projection(v);
      Chop._10.requireClose(v, v2);
      LinearSubspace linearSubspace = LinearSubspace.of(tStMemberQ::defect, k, n);
      Tensor v3 = linearSubspace.projection(c);
      Chop._10.requireClose(v, v3);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void test(int n) {
    for (int k = n - 2; k < n; ++k) {
      StiefelManifold stiefelManifold = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(stiefelManifold);
      Tensor g = Transpose.of(p).dot(p);
      assertTrue(GrManifold.INSTANCE.isMember(g));
    }
  }
}
