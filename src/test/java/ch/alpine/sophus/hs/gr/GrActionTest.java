// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogisticDistribution;
import ch.alpine.tensor.sca.Chop;

class GrActionTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6, 10 })
  void testDecomp(int n) {
    for (int k = 1; k <= n; ++k) {
      RandomSampleInterface randomSampleInterface = new Grassmannian(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      GrAction so = GrAction.match(p, q);
      Chop._12.requireClose(so.apply(p), q);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6, 10 })
  void testSimple(int n) {
    RandomGenerator randomGenerator = new Random(3);
    for (int k = 1; k <= n; ++k) {
      Grassmannian grassmannian = new Grassmannian(n, k);
      Tensor p = RandomSample.of(grassmannian, randomGenerator);
      Tensor q = RandomSample.of(grassmannian, randomGenerator);
      Distribution distribution = LogisticDistribution.of(1, 3);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
      Tensor pv = tGrMemberQ.projection(RandomVariate.of(distribution, randomGenerator, n, n));
      Tensor log = new GrExponential(p).log(q);
      tGrMemberQ.require(log);
      HsTransport hsTransport = grassmannian.hsTransport();
      Tensor qv = hsTransport.shift(p, q).apply(pv);
      new TGrMemberQ(q).require(qv);
      GrAction match = GrAction.match(p, q);
      Tensor ofForm = BasisTransform.ofForm(pv, match.g());
      assumeTrue(false);
      // TODO SOPHUS pv is not a "form" but a tangent vector!
      Chop._08.requireClose(qv, ofForm);
    }
  }
}
