// code by jph
package showcase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.gr.Grassmannian;
import ch.alpine.sophus.hs.h.Hyperboloid;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.hs.st.StiefelManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class SampleManifolds {
  public static List<Manifold> list() {
    return Arrays.asList( //
        new StiefelManifold(3, 1), //
        new Grassmannian(5, 2), //
        new RnGroup(3), //
        Se2Group.INSTANCE, //
        Se2CoveringGroup.INSTANCE, //
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
  void testDistance(Manifold manifold) {
    assumeTrue(manifold instanceof RandomSampleInterface);
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
  void testExponential(Manifold manifold) {
    assumeTrue(manifold instanceof RandomSampleInterface);
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
}
