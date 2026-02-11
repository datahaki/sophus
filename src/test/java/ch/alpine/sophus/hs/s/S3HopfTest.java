// code by jph
package ch.alpine.sophus.hs.s;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class S3HopfTest {
  @Test
  void testSimple() {
    Sphere sphere = new Sphere(3);
    Tensor xyza = RandomSample.of(sphere);
    S3Hopf s3Hopf = S3Hopf.of(xyza);
    Tensor h1 = s3Hopf.project();
    Scalar angle = RandomVariate.of(UniformDistribution.of(Clips.absolute(Pi.VALUE)));
    Tensor lift = s3Hopf.lift(angle);
    Tolerance.CHOP.requireClose(h1, S3Hopf.of(lift).project());
  }

  @Test
  void testLift() {
    Sphere sphere = new Sphere(2);
    Tensor xyz = RandomSample.of(sphere);
    S3Hopf.northernHemisphereGauge(xyz);
    S3Hopf.stereographic(xyz);
  }
}
