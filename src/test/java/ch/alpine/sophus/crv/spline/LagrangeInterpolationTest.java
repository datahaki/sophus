// code by jph
package ch.alpine.sophus.crv.spline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

public class LagrangeInterpolationTest {
  @Test
  public void testSimple() {
    Tensor control = RandomVariate.of(DiscreteUniformDistribution.of(-3, 7), 4, 7).unmodifiable();
    Interpolation interpolation = LagrangeInterpolation.of(RnGeodesic.INSTANCE, control);
    Tensor domain = Range.of(0, control.length());
    Tensor polynom = domain.map(interpolation::at);
    assertEquals(control, polynom);
    ExactTensorQ.require(polynom);
  }

  @Test
  public void testFail() {
    Tensor control = RandomVariate.of(DiscreteUniformDistribution.of(-3, 7), 4, 2).unmodifiable();
    Interpolation interpolation = LagrangeInterpolation.of(RnGeodesic.INSTANCE, control);
    interpolation.get(Tensors.vector(1));
    AssertFail.of(() -> interpolation.get(Tensors.vector(1, 2)));
  }
}
