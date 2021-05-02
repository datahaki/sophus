// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.IOException;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.LogisticDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrTransportTest extends TestCase {
  public static final HsTransport POLE_LADDER = PoleLadder.of(GrManifold.INSTANCE);

  public void testSimple() throws ClassNotFoundException, IOException {
    int n = 4;
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, 2);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Distribution distribution = LogisticDistribution.of(1, 3);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    Tensor pv = tGrMemberQ.forceProject(RandomVariate.of(distribution, n, n));
    Tensor log = new GrExponential(p).log(q);
    tGrMemberQ.require(log);
    Tensor qv0 = POLE_LADDER.shift(p, q).apply(pv);
    Tensor qv1 = Serialization.copy(GrTransport.INSTANCE.shift(p, q)).apply(pv);
    new TGrMemberQ(q).require(qv1);
    Chop._08.requireClose(qv0, qv1);
    Tensor match = GrAction.match(p, q);
    Tensor ofForm = BasisTransform.ofForm(pv, match);
    // Tensor qw = GrTransport2.INSTANCE.shift(p, q).apply(pv);
    // System.out.println(Pretty.of(qv.map(Round._3)));
    // System.out.println(Pretty.of(qw.map(Round._3)));
    Chop._08.isClose(qv1, ofForm); // this is not correct
  }

  public void testFromOToP() {
    int n = 5;
    for (int k = 0; k <= n; ++k) {
      int fk = k;
      Distribution distribution = UniformDistribution.unit();
      TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(n, k);
      Tensor ov = tGr0MemberQ.project(RandomVariate.of(distribution, n, n));
      Tensor o = DiagonalMatrix.with(Tensors.vector(i -> Boole.of(i < fk), n));
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      TensorUnaryOperator tensorUnaryOperator = GrTransport.INSTANCE.shift(o, p);
      Tensor pv = tensorUnaryOperator.apply(ov);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
      tGrMemberQ.require(pv);
    }
  }

  public void testNonMemberFail() {
    int n = 5;
    for (int k = 1; k < n; ++k) {
      Distribution distribution = UniformDistribution.unit();
      RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      TensorUnaryOperator tensorUnaryOperator = GrTransport.INSTANCE.shift(p, q);
      Tensor ov = RandomVariate.of(distribution, n, n);
      AssertFail.of(() -> tensorUnaryOperator.apply(ov));
    }
  }
}