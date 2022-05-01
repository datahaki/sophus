// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.gr.InfluenceMatrixQ;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class GrExponentialTest {
  @Test
  public void test0D() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = tGrMemberQ.forceProject(pre);
    tGrMemberQ.require(v);
    Chop.NONE.requireAllZero(v);
    Tensor exp = grExponential.exp(v);
    InfluenceMatrixQ.require(exp);
    Tolerance.CHOP.requireClose(x, exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireAllZero(log);
    Tensor vectorLog = grExponential.vectorLog(exp);
    Tolerance.CHOP.requireAllZero(vectorLog);
  }

  @Test
  public void testSimple() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 0}}");
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = tGrMemberQ.forceProject(pre);
    tGrMemberQ.require(v);
    Tensor exp = grExponential.exp(v);
    InfluenceMatrixQ.require(exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
  }

  @Test
  public void testShift() {
    Tensor x = RandomSample.of(new GrRandomSample(2, 1));
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Distribution distribution = UniformDistribution.unit();
    Tensor pre = RandomVariate.of(distribution, 2, 2);
    Tensor v = tGrMemberQ.forceProject(pre);
    tGrMemberQ.require(v);
    Tensor exp = grExponential.exp(v);
    assertTrue(InfluenceMatrixQ.of(exp));
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
    grExponential.vectorLog(exp);
  }

  @ParameterizedTest
  @ValueSource(ints = { 4, 5, 6 })
  public void testDesign(int n) {
    int k = 3;
    Tensor x = RandomSample.of(new GrRandomSample(n, k));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    assertEquals(MatrixRank.of(x), k);
    InfluenceMatrixQ.require(x);
    GrExponential grExponential = new GrExponential(x);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(x);
    Tensor pre = RandomVariate.of(NormalDistribution.of(0.0, 0.1), n, n);
    Tensor v = tGrMemberQ.forceProject(pre);
    tGrMemberQ.require(v);
    assertFalse(Chop._05.allZero(v));
    Tensor exp = grExponential.exp(v);
    InfluenceMatrixQ.require(exp);
    assertTrue(Scalars.lessThan(RealScalar.of(0.001), FrobeniusNorm.between(x, exp)));
    Tensor w = grExponential.log(exp);
    Chop._05.requireClose(v, w);
  }

  @Test
  public void testGrFail() {
    Tensor x = Tensors.fromString("{{1, 1}, {0, 1}}");
    assertThrows(Exception.class, () -> new GrExponential(x));
  }
}
