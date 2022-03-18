// code by jph
package ch.alpine.sophus.crv.se2c;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

public class EulerSpiralTest {
  @Test
  public void testSpecific() {
    Tensor xya = EulerSpiral.FUNCTION.apply(RealScalar.of(0.3));
    Tolerance.CHOP.requireClose(xya, Tensors.vector(0.2994009760520471, 0.014116998006574732, 0.1413716694115407));
  }

  @Test
  public void testAngle() {
    Tensor xya = EulerSpiral.FUNCTION.apply(RealScalar.of(Math.sqrt(2 / Math.PI)));
    Tolerance.CHOP.requireClose(xya, Tensors.vector(0.721705924292615, 0.24755828765159393, 1));
  }

  @Test
  public void testWindings() {
    Tensor xya = EulerSpiral.FUNCTION.apply(RealScalar.of(5));
    Tolerance.CHOP.requireClose(xya, Tensors.vector(0.563631188704011, 0.4991913819171182, 39.269908169872416));
  }

  @Test
  public void testAntiSymmetry() {
    Distribution distribution = UniformDistribution.of(-100, 100);
    for (int count = 0; count < 100; ++count) {
      Scalar scalar = RandomVariate.of(distribution);
      Tensor p = EulerSpiral.FUNCTION.apply(scalar);
      Tensor q = EulerSpiral.FUNCTION.apply(scalar.negate());
      VectorQ.requireLength(p, 3);
      Tolerance.CHOP.requireClose(p.extract(0, 2), q.extract(0, 2).negate());
      Tolerance.CHOP.requireClose(p.get(2), q.get(2));
    }
  }
}
