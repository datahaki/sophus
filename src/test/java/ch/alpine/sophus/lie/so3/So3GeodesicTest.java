// code by jph
package ch.alpine.sophus.lie.so3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class So3GeodesicTest {
  @Test
  public void testSimple() {
    Tensor p = Rodrigues.vectorExp(Tensors.vector(1, 2, 3));
    Tensor q = Rodrigues.vectorExp(Tensors.vector(2, -1, 2));
    Tensor split = So3Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(OrthogonalMatrixQ.of(split, Chop._14));
  }

  @Test
  public void testEndPoints() {
    Distribution distribution = NormalDistribution.of(0, .3);
    for (int index = 0; index < 10; ++index) {
      Tensor p = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor q = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Chop._14.requireClose(p, So3Geodesic.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._11.requireClose(q, So3Geodesic.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }
}
