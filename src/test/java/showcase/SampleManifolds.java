package showcase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.gr.Grassmannian;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.hs.st.StiefelManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;

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
        new Sphere(3) //
    );
  }

  @ParameterizedTest
  @MethodSource("list")
  void testDistance(Manifold manifold) {
    assumeTrue(manifold instanceof RandomSampleInterface);
    RandomSampleInterface rsi = (RandomSampleInterface) manifold;
    assertEquals( //
        RandomSample.of(rsi, new Random(2)), //
        RandomSample.of(rsi, new Random(2)));
    assumeTrue(manifold instanceof MetricManifold);
    MetricManifold metricManifold = (MetricManifold) manifold;
    Tensor p = RandomSample.of(rsi);
    Tensor q = RandomSample.of(rsi);
    assumeFalse(ThrowQ.of(() -> manifold.exponential(p).log(q)));
    Scalar d_pq = metricManifold.distance(p, q);
    Scalar d_qp = metricManifold.distance(p, q);
    Tolerance.CHOP.requireClose(d_pq, d_qp);
    assumeTrue(manifold instanceof GeodesicSpace);
    GeodesicSpace geodesicSpace = (GeodesicSpace) manifold;
    Tensor m = geodesicSpace.midpoint(p, q);
    Scalar d_pm = metricManifold.distance(p, m);
    Scalar d_mq = metricManifold.distance(m, q);
    Tolerance.CHOP.requireClose(d_pq, d_pm.add(d_mq));
  }
}
