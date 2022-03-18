// code by jph
package ch.alpine.sophus.clt;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;

public class ClothoidDistanceTest {
  @Test
  public void testSimple() {
    Chop._10.requireClose(ClothoidDistance.SE2_ANALYTIC.norm(Tensors.vector(10, 0, 0)), RealScalar.of(10));
    Chop._10.requireClose(ClothoidDistance.SE2_COVERING.norm(Tensors.vector(10, 0, 0)), RealScalar.of(10));
    Chop._10.requireClose(ClothoidDistance.SE2_ANALYTIC.norm(Tensors.vector(23, 0, 0)), RealScalar.of(23));
    Chop._10.requireClose(ClothoidDistance.SE2_COVERING.norm(Tensors.vector(23, 0, 0)), RealScalar.of(23));
  }
}
