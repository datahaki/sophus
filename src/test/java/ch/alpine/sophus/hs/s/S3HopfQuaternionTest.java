// code by jph
package ch.alpine.sophus.hs.s;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.Quaternion;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class S3HopfQuaternionTest {
  @Test
  void test() {
    Sphere sphere = new Sphere(3);
    Tensor xyza = RandomSample.of(sphere);
    Quaternion q = Quaternion.of(xyza.Get(3), xyza.extract(0, 3));
    S3HopfQuaternion s3HopfQuaternion = new S3HopfQuaternion(q);
    Tensor xyz = s3HopfQuaternion.project();
    Scalar angle = RandomVariate.of(UniformDistribution.of(Clips.absolute(Pi.VALUE)));
    Tensor tensor = s3HopfQuaternion.lift(angle);
    Tolerance.CHOP.requireClose(Vector2Norm.of(tensor), RealScalar.ONE);
    // S3HopfQuaternion s3 = S3HopfQuaternion.northernHemisphereGauge(xyz);
  }
}
