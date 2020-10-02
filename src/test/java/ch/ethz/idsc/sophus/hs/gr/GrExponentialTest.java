// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.mat.MatrixRank;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrExponentialTest extends TestCase {
  private static final HsMemberQ HS_MEMBER_Q = GrMemberQ.of(Tolerance.CHOP);

  public void test0D() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.projectTangent(x, pre);
    HS_MEMBER_Q.requireTangent(x, v);
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
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.projectTangent(x, pre);
    HS_MEMBER_Q.requireTangent(x, v);
    Tensor exp = grExponential.exp(v);
    GrassmannQ.require(exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
  }

  public void testShift() {
    Tensor x = StaticHelper.projection(Tensors.vector(0.2, 0.5));
    GrExponential grExponential = new GrExponential(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = StaticHelper.projectTangent(x, pre);
    Tensor exp = grExponential.exp(v);
    assertTrue(GrassmannQ.of(exp));
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
    grExponential.vectorLog(exp);
  }

  public void testDesign() {
    Distribution distribution = UniformDistribution.unit();
    int rank = 3;
    for (int n = 4; n < 7; ++n) {
      TangentSpace tangentSpace = RnManifold.INSTANCE.logAt(RandomVariate.of(distribution, rank));
      HsInfluence hsInfluence = new HsInfluence(tangentSpace, RandomVariate.of(distribution, n, rank));
      Tensor x = hsInfluence.matrix();
      assertEquals(Dimensions.of(x), Arrays.asList(n, n));
      assertEquals(MatrixRank.of(x), rank);
      GrassmannQ.require(x);
      GrExponential grExponential = new GrExponential(x);
      Tensor pre = RandomVariate.of(distribution, n, n);
      Tensor v = StaticHelper.projectTangent(x, pre);
      assertFalse(Chop._05.allZero(v));
      Tensor exp = grExponential.exp(v);
      GrassmannQ.require(exp);
      assertTrue(Scalars.lessThan(RealScalar.of(0.001), Frobenius.between(x, exp)));
    }
  }

  public void testGrFail() {
    Tensor x = Tensors.fromString("{{1, 1}, {0, 1}}");
    AssertFail.of(() -> new GrExponential(x));
  }
}
