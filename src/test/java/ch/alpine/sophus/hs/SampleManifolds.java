// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.usr.SophusExperimental;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.TensorUnaryOperator;
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

class SampleManifolds {
  public static List<HomogeneousSpace> homogeneousSpaces() {
    return SophusExperimental.filter(HomogeneousSpace.class);
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testSerialization(HomogeneousSpace homogeneousSpace) throws ClassNotFoundException, IOException {
    Serialization.copy(homogeneousSpace);
    Serialization.copy(homogeneousSpace.isPointQ());
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    assertEquals( //
        RandomSample.of(rsi, new Random(13)), //
        RandomSample.of(rsi, new Random(13)));
    Tensor p = RandomSample.of(rsi);
    Exponential exponential = homogeneousSpace.exponential(p);
    Serialization.copy(exponential);
    Serialization.copy(exponential.isTangentQ());
    Tolerance.CHOP.requireClose(homogeneousSpace.flip(p, p), p);
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testDistance(HomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    assumeTrue(homogeneousSpace instanceof MetricManifold);
    MetricManifold metricManifold = (MetricManifold) homogeneousSpace;
    Tensor p = RandomSample.of(rsi);
    Exponential exponential = homogeneousSpace.exponential(p);
    Tensor q = RandomSample.of(LocalRandomSample.of(exponential, p, 0.1));
    assumeFalse(ThrowQ.of(() -> exponential.log(q)));
    Scalar d_pq = metricManifold.distance(p, q);
    Scalar d_qp = metricManifold.distance(q, p);
    Chop._10.requireClose(d_pq, d_qp);
    assumeTrue(homogeneousSpace instanceof GeodesicSpace);
    GeodesicSpace geodesicSpace = (GeodesicSpace) homogeneousSpace;
    Tensor m = geodesicSpace.midpoint(p, q);
    Scalar d_pm = metricManifold.distance(p, m);
    Scalar d_mq = metricManifold.distance(m, q);
    Tolerance.CHOP.requireClose(d_pq, d_pm.add(d_mq));
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testExponential(HomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    Tensor p = RandomSample.of(rsi);
    Exponential exponential = homogeneousSpace.exponential(p);
    Tensor q = RandomSample.of(LocalRandomSample.of(exponential, p, 0.1));
    Tensor log_p = exponential.log(p);
    Tolerance.CHOP.requireAllZero(log_p);
    assumeFalse(ThrowQ.of(() -> exponential.log(q)));
    Tensor v = exponential.log(q);
    List<Integer> list = Dimensions.of(v);
    assertEquals(list, Dimensions.of(log_p));
    ZeroDefectArrayQ zeroDefectArrayQ = exponential.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, list);
    int d = linearSubspace.dimensions();
    Tensor weights = RandomVariate.of(NormalDistribution.of(0.0, 0.1), d);
    Tensor w = linearSubspace.apply(weights);
    Tensor r = exponential.exp(w);
    assumeTrue(homogeneousSpace.isPointQ().test(r));
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testBiinvMean(HomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    Tensor p = RandomSample.of(rsi);
    Exponential exponential = homogeneousSpace.exponential(p);
    Tensor log_p = exponential.log(p);
    List<Integer> list = Dimensions.of(log_p);
    Tolerance.CHOP.requireAllZero(log_p);
    ZeroDefectArrayQ zeroDefectArrayQ = exponential.isTangentQ();
    zeroDefectArrayQ.require(log_p);
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, list);
    int d = linearSubspace.dimensions();
    if (homogeneousSpace instanceof SpecificManifold specificManifold) {
      assertEquals(specificManifold.dimensions(), d);
    }
    {
      int n = d + 3;
      RandomSampleInterface rpnts = LocalRandomSample.of(exponential, p, RealScalar.of(0.05));
      Tensor sequence = RandomSample.of(rpnts, n);
      BiinvariantMean biinvariantMean = homogeneousSpace.biinvariantMean();
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      biinvariantMean.mean(sequence, weights);
    }
    {
      RandomSampleInterface rpnts = LocalRandomSample.of(exponential, p, RealScalar.of(0.1));
      Tensor sequence = RandomSample.of(rpnts, d);
      Tensor slash1 = exponential.vectorLog().slash(sequence);
      TensorUnaryOperator vectorLog = t -> Flatten.of(exponential.log(t));
      Tensor slash2 = vectorLog.slash(sequence);
      int rank = MatrixRank.of(slash2);
      assertEquals(MatrixRank.of(slash1), rank);
      assertEquals(rank, d);
    }
  }

  @ParameterizedTest
  @MethodSource("homogeneousSpaces")
  void testTransport(HomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    final Tensor p = RandomSample.of(rsi);
    final Exponential exp_p = homogeneousSpace.exponential(p);
    final Tensor q = RandomSample.of(LocalRandomSample.of(exp_p, p, 0.05));
    final Exponential exp_q = homogeneousSpace.exponential(q);
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
