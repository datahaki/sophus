// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.api.MetricManifold;
import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.rsm.LocalRandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ArrayQ;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.pi.LinearSubspaceMemberQ;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import test.wrap.ThrowQ;

class HomogeneousSpaceTest {
  static List<SpecificHomogeneousSpace> homogeneousSpaces() {
    return SophusExperimental.filter(SpecificHomogeneousSpace.class);
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testSimpleWert(SpecificHomogeneousSpace homogeneousSpace) {
    Tensor p = RandomSample.of(homogeneousSpace.randomSampleInterface());
    homogeneousSpace.isPointQ().require(p);
    List<Integer> dims = Dimensions.of(p);
    TangentSpace tangentSpace = homogeneousSpace.tangentSpace(p);
    Tensor v = tangentSpace.log(p);
    ArrayQ.require(v);
    Tolerance.CHOP.requireAllZero(v);
    assertEquals(dims, Dimensions.of(v));
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testSerialization(SpecificHomogeneousSpace homogeneousSpace) {
    assertDoesNotThrow(() -> Serialization.copy(homogeneousSpace));
    MemberQ pointQ = homogeneousSpace.isPointQ();
    RandomSampleInterface rsi = homogeneousSpace.randomSampleInterface();
    assertEquals( //
        RandomSample.of(rsi, new Random(13)), //
        RandomSample.of(rsi, new Random(13)));
    Tensor p = RandomSample.of(rsi);
    pointQ.require(p);
    TangentSpace exponential = homogeneousSpace.tangentSpace(p);
    assertDoesNotThrow(() -> exponential);
    assertDoesNotThrow(() -> exponential.isTangentQ());
    Tolerance.CHOP.requireClose(homogeneousSpace.flip(p, p), p);
    assertDoesNotThrow(() -> homogeneousSpace.biinvariantMean());
    assertDoesNotThrow(() -> homogeneousSpace.hsTransport());
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testDistance(SpecificHomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = homogeneousSpace.randomSampleInterface();
    assumeTrue(homogeneousSpace instanceof MetricManifold);
    MetricManifold metricManifold = (MetricManifold) homogeneousSpace;
    Tensor p = RandomSample.of(rsi);
    TangentSpace tangentSpace = homogeneousSpace.tangentSpace(p);
    Tensor q = RandomSample.of(LocalRandomSample.of(tangentSpace, 0.1));
    assumeFalse(ThrowQ.of(() -> tangentSpace.log(q)));
    Scalar d_pq = metricManifold.distance(p, q);
    Scalar d_qp = metricManifold.distance(q, p);
    Chop._10.requireClose(d_pq, d_qp);
    GeodesicSpace geodesicSpace = homogeneousSpace;
    Tensor m = geodesicSpace.midpoint(p, q);
    Scalar d_pm = metricManifold.distance(p, m);
    Scalar d_mq = metricManifold.distance(m, q);
    Tolerance.CHOP.requireClose(d_pq, d_pm.add(d_mq));
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testExponential(SpecificHomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = homogeneousSpace.randomSampleInterface();
    Tensor p = RandomSample.of(rsi);
    TangentSpace tangentSpace = homogeneousSpace.tangentSpace(p);
    Tensor q = RandomSample.of(LocalRandomSample.of(tangentSpace, 0.1));
    Tensor log_p = tangentSpace.log(p);
    Tolerance.CHOP.requireAllZero(log_p);
    assumeFalse(ThrowQ.of(() -> tangentSpace.log(q)));
    Tensor v = tangentSpace.log(q);
    List<Integer> list = Dimensions.of(v);
    assertEquals(list, Dimensions.of(log_p));
    ZeroDefectArrayQ zeroDefectArrayQ = tangentSpace.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, list);
    int d = linearSubspace.dimensions();
    Tensor weights = RandomVariate.of(NormalDistribution.of(0.0, 0.1), d);
    Tensor w = linearSubspace.apply(weights);
    Tensor r = tangentSpace.exp(w);
    assumeTrue(homogeneousSpace.isPointQ().test(r));
    Tensor pv100 = tangentSpace.exp(v.multiply(RealScalar.of(10)));
    MemberQ pointQ = homogeneousSpace.isPointQ();
    pointQ.require(pv100);
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testBiinvMean(SpecificHomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = homogeneousSpace.randomSampleInterface();
    Tensor p = RandomSample.of(rsi);
    TangentSpace tangentSpace = homogeneousSpace.tangentSpace(p);
    Tensor log_p = tangentSpace.log(p);
    List<Integer> list = Dimensions.of(log_p);
    Tolerance.CHOP.requireAllZero(log_p);
    ZeroDefectArrayQ zeroDefectArrayQ = tangentSpace.isTangentQ();
    zeroDefectArrayQ.require(log_p);
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, list);
    int d = linearSubspace.dimensions();
    if (homogeneousSpace instanceof SpecificHomogeneousSpace specificManifold) {
      assertEquals(specificManifold.dimensions(), d);
    }
    {
      int n = d + 3;
      RandomSampleInterface rpnts = LocalRandomSample.of(tangentSpace, RealScalar.of(0.05));
      Tensor sequence = RandomSample.of(rpnts, n);
      BiinvariantMean biinvariantMean = homogeneousSpace.biinvariantMean();
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      biinvariantMean.mean(sequence, weights);
    }
    {
      RandomSampleInterface rpnts = LocalRandomSample.of(tangentSpace, RealScalar.of(0.1));
      Tensor sequence = RandomSample.of(rpnts, d);
      Tensor slash1 = tangentSpace.vectorLog().slash(sequence);
      TensorUnaryOperator vectorLog = t -> Flatten.of(tangentSpace.log(t));
      Tensor slash2 = vectorLog.slash(sequence);
      int rank = MatrixRank.of(slash2);
      assertEquals(MatrixRank.of(slash1), rank);
      assertEquals(rank, d);
    }
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testTransport(SpecificHomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = homogeneousSpace.randomSampleInterface();
    final Tensor p = RandomSample.of(rsi);
    final TangentSpace exp_p = homogeneousSpace.tangentSpace(p);
    final Tensor q = RandomSample.of(LocalRandomSample.of(exp_p, 0.05));
    final TangentSpace exp_q = homogeneousSpace.tangentSpace(q);
    TensorUnaryOperator shift = homogeneousSpace.hsTransport().shift(p, q);
    {
      Tensor v = exp_p.log(q);
      Tensor pqv = shift.apply(v);
      Tensor qpn = exp_q.log(p).negate();
      Chop._08.requireClose(pqv, qpn);
    }
    Tensor log_p = exp_p.log(p);
    LinearSubspace sub_p = LinearSubspace.of(exp_p.isTangentQ()::defect, Dimensions.of(log_p));
    LinearSubspace sub_q = LinearSubspace.of(exp_q.isTangentQ()::defect, Dimensions.of(log_p));
    Tensor pqb = shift.slash(sub_p.basis());
    ZeroDefectArrayQ zeroDefectArrayQ = LinearSubspaceMemberQ.of(sub_q, Chop._10);
    boolean success = pqb.stream().allMatch(zeroDefectArrayQ);
    assumeTrue(success);
  }
}
