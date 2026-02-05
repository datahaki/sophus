package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Im;

class StBilinearFormTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6, 10 })
  void testSimple(int n) throws ClassNotFoundException, IOException {
    RandomGenerator randomGenerator = new Random(1);
    for (int k = 1; k <= n; ++k) {
      StiefelManifold stiefelManifold = new StiefelManifold(n, k);
      Tensor p = RandomSample.of(stiefelManifold, randomGenerator);
      assertTrue(Im.allZero(p));
      stiefelManifold.requireMember(p);
      TStMemberQ tStMemberQ = new TStMemberQ(p);
      Tensor v = tStMemberQ.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), randomGenerator, k, n));
      assertTrue(Im.allZero(v));
      BilinearForm bilinearForm = stiefelManifold.bilinearForm(p);
      Scalar norm = bilinearForm.norm(v);
      Exponential exponential = Serialization.copy(stiefelManifold.exponential(p));
      Tensor q = exponential.exp(v);
      assertTrue(Im.allZero(q));
      Tensor w = exponential.log(q);
      assertTrue(Im.allZero(w));
      Scalar d_pq = stiefelManifold.distance(p, q);
      Scalar d_qp = stiefelManifold.distance(q, p);
      Tolerance.CHOP.requireClose(norm, d_pq);
      Tolerance.CHOP.requireClose(d_pq, d_qp);
      Tensor u = tStMemberQ.projection(RandomVariate.of(NormalDistribution.of(0.0, 0.1), randomGenerator, k, n));
      Scalar eval1 = bilinearForm.formEval(u, v);
      Scalar eval2 = bilinearForm.formEval(v, u);
      Tolerance.CHOP.requireClose(eval1, eval2);
      SoNGroup Gk = new SoNGroup(k);
      SoNGroup Gn = new SoNGroup(n);
      StAction stAction = new StAction(RandomSample.of(Gk), RandomSample.of(Gn));
      Tensor ap = stAction.apply(p);
      stiefelManifold.requireMember(ap);
      Tensor aq = stAction.apply(q);
      Scalar d_apq = stiefelManifold.distance(ap, aq);
      Tolerance.CHOP.requireClose(d_pq, d_apq);
      {
        Tensor m = stiefelManifold.midpoint(p, q);
        Scalar d_pm = stiefelManifold.distance(p, m);
        Scalar d_qm = stiefelManifold.distance(q, m);
        Tolerance.CHOP.requireClose(d_pm.add(d_qm), d_pq);
      }
    }
  }
}
