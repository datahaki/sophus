// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class GeodesicNevilleTest extends TestCase {
  public void testScaleInvariant() {
    Tensor suppor = Tensors.vector(2, 2.3, 4);
    Tensor values = Tensors.vector(6, -7, 20);
    ScalarTensorFunction gn1 = GeodesicNeville.of(RnGeodesic.INSTANCE, suppor, values);
    ScalarTensorFunction gn2 = GeodesicNeville.of(RnGeodesic.INSTANCE, //
        suppor.multiply(RealScalar.of(3)).map(RealScalar.ONE::subtract), values);
    Distribution distribution = UniformDistribution.of(2, 4);
    Tensor domain = RandomVariate.of(distribution, 20);
    Tolerance.CHOP.requireClose( //
        domain.map(gn1), //
        domain.multiply(RealScalar.of(3)).map(RealScalar.ONE::subtract).map(gn2));
  }

  public void testScaleInvariantMatrix() {
    Tensor suppor = Tensors.vector(2, 2.3, 4);
    Tensor values = Tensors.fromString("{{2,-3}, {-7, 5}, {5, 9}}");
    ScalarTensorFunction gn1 = GeodesicNeville.of(RnGeodesic.INSTANCE, suppor, values);
    ScalarTensorFunction gn2 = GeodesicNeville.of(RnGeodesic.INSTANCE, //
        suppor.multiply(RealScalar.of(3)).map(RealScalar.ONE::subtract), values);
    Distribution distribution = UniformDistribution.of(2, 4);
    Tensor domain = RandomVariate.of(distribution, 20);
    Tolerance.CHOP.requireClose( //
        domain.map(gn1), //
        domain.multiply(RealScalar.of(3)).map(RealScalar.ONE::subtract).map(gn2));
  }

  public void testLengthFail() {
    try {
      GeodesicNeville.of(RnGeodesic.INSTANCE, Tensors.vector(1, 2, 3), Tensors.vector(1, 2));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
