// code by jph
package showcase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.sophus.hs.gr.Grassmannian;
import ch.alpine.sophus.hs.h.Hyperboloid;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.hs.st.StiefelManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class SampleManifolds {
  public static List<HomogeneousSpace> list() {
    return Arrays.asList( //
        new StiefelManifold(3, 1), //
        new Grassmannian(5, 2), //
        new RnGroup(3), //
        Se2Group.INSTANCE, //
        Se2CoveringGroup.INSTANCE, //
        new SeNGroup(3), //
        new SeNGroup(4), //
        new SoNGroup(2), //
        new SoNGroup(3), //
        new SoNGroup(4), //
        new Sphere(2), //
        new Sphere(3), //
        new Hyperboloid(2), //
        new Hyperboloid(3), //
        new Hyperboloid(4) //
    );
  }

  @ParameterizedTest
  @MethodSource("list")
  void testSerialization(HomogeneousSpace manifold) throws ClassNotFoundException, IOException {
    Serialization.copy(manifold);
    Serialization.copy(manifold.isPointQ());
    RandomSampleInterface rsi = (RandomSampleInterface) manifold;
    Tensor p = RandomSample.of(rsi);
    Exponential exponential = manifold.exponential(p);
    Serialization.copy(exponential);
    Serialization.copy(exponential.isTangentQ());
    Tolerance.CHOP.requireClose(manifold.flip(p, p), p);
  }

  @ParameterizedTest
  @MethodSource("list")
  void testDistance(HomogeneousSpace manifold) {
    RandomSampleInterface rsi = (RandomSampleInterface) manifold;
    assertEquals( //
        RandomSample.of(rsi, new Random(13)), //
        RandomSample.of(rsi, new Random(13)));
    assumeTrue(manifold instanceof MetricManifold);
    MetricManifold metricManifold = (MetricManifold) manifold;
    Tensor p = RandomSample.of(rsi);
    Tensor q = RandomSample.of(rsi);
    Exponential exponential = manifold.exponential(p);
    assumeFalse(ThrowQ.of(() -> exponential.log(q)));
    Scalar d_pq = metricManifold.distance(p, q);
    Scalar d_qp = metricManifold.distance(q, p);
    Chop._10.requireClose(d_pq, d_qp);
    assumeTrue(manifold instanceof GeodesicSpace);
    GeodesicSpace geodesicSpace = (GeodesicSpace) manifold;
    Tensor m = geodesicSpace.midpoint(p, q);
    Scalar d_pm = metricManifold.distance(p, m);
    Scalar d_mq = metricManifold.distance(m, q);
    Tolerance.CHOP.requireClose(d_pq, d_pm.add(d_mq));
  }

  @ParameterizedTest
  @MethodSource("list")
  void testExponential(HomogeneousSpace manifold) {
    RandomSampleInterface rsi = (RandomSampleInterface) manifold;
    assertEquals( //
        RandomSample.of(rsi, new Random(13)), //
        RandomSample.of(rsi, new Random(13)));
    Tensor p = RandomSample.of(rsi);
    Tensor q = RandomSample.of(rsi);
    Exponential exponential = manifold.exponential(p);
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
    assumeTrue(manifold.isPointQ().test(r));
  }

  @ParameterizedTest
  @MethodSource("list")
  void testBiinvMean(HomogeneousSpace homogeneousSpace) {
    RandomSampleInterface rsi = (RandomSampleInterface) homogeneousSpace;
    assertEquals( //
        RandomSample.of(rsi, new Random(13)), //
        RandomSample.of(rsi, new Random(13)));
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
    int n = d + 3;
    Tensor coeffs = RandomVariate.of(NormalDistribution.of(0.0, 0.02), n, d);
    Tensor tangents = linearSubspace.slash(coeffs);
    Tensor sequence = exponential.exp().slash(tangents);
    BiinvariantMean biinvariantMean = homogeneousSpace.biinvariantMean();
    Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
    // FIXME
    if (!homogeneousSpace.toString().startsWith("SO["))
      biinvariantMean.mean(sequence, weights);
  }
}
