// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.num.Boole;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.LogisticDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrTransportTest extends TestCase {
  public void testSimple() {
    int n = 4;
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(n, 2);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Distribution distribution = LogisticDistribution.of(1, 3);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    Tensor pv = StaticHelper.project(p, RandomVariate.of(distribution, n, n));
    Tensor log = new GrExponential(p).log(q);
    tGrMemberQ.require(log);
    Tensor qv = GrTransport.INSTANCE.shift(p, q).apply(pv);
    new TGrMemberQ(q).require(qv);
    Tensor match = GrAction.match(p, q);
    Tensor ofForm = BasisTransform.ofForm(pv, match);
    // Tensor qw = GrTransport2.INSTANCE.shift(p, q).apply(pv);
    // System.out.println(Pretty.of(qv.map(Round._3)));
    // System.out.println(Pretty.of(qw.map(Round._3)));
    Chop._08.isClose(qv, ofForm); // this is not correct
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
