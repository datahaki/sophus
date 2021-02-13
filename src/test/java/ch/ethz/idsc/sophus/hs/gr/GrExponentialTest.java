// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.util.Arrays;

import ch.ethz.idsc.sophus.math.GrassmannQ;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.mat.MatrixRank;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.FrobeniusNorm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrExponentialTest extends TestCase {
  public void test0D() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.project(x, pre);
    tGrMemberQ.require(v);
    Chop.NONE.requireAllZero(v);
    Tensor exp = grExponential.exp(v);
    GrassmannQ.require(exp);
    Tolerance.CHOP.requireClose(x, exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireAllZero(log);
    Tensor vectorLog = grExponential.vectorLog(exp);
    Tolerance.CHOP.requireAllZero(vectorLog);
  }

  public void testSimple() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 0}}");
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.project(x, pre);
    tGrMemberQ.require(v);
    Tensor exp = grExponential.exp(v);
    GrassmannQ.require(exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
  }

  public void testShift() {
    Tensor x = RandomSample.of(GrRandomSample.of(2, 1));
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.project(x, pre);
    tGrMemberQ.require(v);
    Tensor exp = grExponential.exp(v);
    assertTrue(GrassmannQ.of(exp));
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
    grExponential.vectorLog(exp);
  }

  public void testDesign() {
    int k = 3;
    for (int n = 4; n < 7; ++n) {
      Tensor x = RandomSample.of(GrRandomSample.of(n, k));
      assertEquals(Dimensions.of(x), Arrays.asList(n, n));
      assertEquals(MatrixRank.of(x), k);
      GrassmannQ.require(x);
      GrExponential grExponential = new GrExponential(x);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
      Tensor pre = RandomVariate.of(NormalDistribution.of(0.0, 0.1), n, n);
      Tensor v = StaticHelper.project(x, pre);
      tGrMemberQ.require(v);
      assertFalse(Chop._05.allZero(v));
      Tensor exp = grExponential.exp(v);
      GrassmannQ.require(exp);
      assertTrue(Scalars.lessThan(RealScalar.of(0.001), FrobeniusNorm.between(x, exp)));
      Tensor w = grExponential.log(exp);
      Chop._05.requireClose(v, w);
    }
  }

  public void testGrFail() {
    Tensor x = Tensors.fromString("{{1, 1}, {0, 1}}");
    AssertFail.of(() -> new GrExponential(x));
  }
}
