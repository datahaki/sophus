// code by jph
package ch.alpine.sophus.hs.h2;

import java.io.IOException;
import java.util.Optional;

import ch.alpine.sophus.fit.SphereFit;
import ch.alpine.sophus.math.sample.BallRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.sophus.ref.d1.LaneRiesenfeldCurveSubdivision;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class H2MidpointTest extends TestCase {
  public void testSymmetric() {
    RandomSampleInterface randomSampleInterface = BallRandomSample.of(Array.zeros(2), RealScalar.ONE);
    for (int count = 0; count < 10; ++count) {
      Tensor a = RandomSample.of(randomSampleInterface);
      Tensor midpoint = H2Midpoint.INSTANCE.midpoint(a, a.negate());
      Chop._12.requireClose(midpoint, Array.zeros(2));
    }
  }

  public void testLine() {
    Tensor midpoint = H2Midpoint.INSTANCE.midpoint(Tensors.vector(0.5, 0.5), Tensors.vector(0.25, 0.25));
    Chop._12.requireClose(midpoint.Get(0), midpoint.Get(1));
  }

  public void testCircle() throws IOException, ClassNotFoundException {
    CurveSubdivision curveSubdivision = //
        Serialization.copy(LaneRiesenfeldCurveSubdivision.of(H2Midpoint.INSTANCE, 1));
    Tensor tensor = Nest.of(curveSubdivision::string, Tensors.fromString("{{0.5, 0}, {0.0, 0.5}}"), 6);
    Optional<SphereFit> optional = SphereFit.of(tensor);
    SphereFit sphereFit = optional.get();
    Chop._12.requireClose(sphereFit.center(), Tensors.vector(1.25, 1.25));
    Chop._12.requireClose(sphereFit.radius(), RealScalar.of(1.457737973711335));
    Tensor residual = Tensor.of(tensor.stream() //
        .map(sphereFit.center()::subtract) //
        .map(Vector2Norm::of) //
        .map(sphereFit.radius()::subtract));
    Chop._12.requireAllZero(residual);
  }
}
