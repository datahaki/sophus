// code by ob, jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.crv.spline.MonomialExtrapolationMask;
import ch.alpine.sophus.flt.CausalFilter;
import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2.Se2Manifold;
import ch.alpine.sophus.math.Geodesic;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class GeodesicIIR2Test extends TestCase {
  public void testSimple() {
    Geodesic geodesicInterface = new HsGeodesic(Se2Manifold.INSTANCE);
    Scalar alpha = RationalScalar.HALF;
    GeodesicIIR2 geodesicIIR2 = new GeodesicIIR2(geodesicInterface, alpha);
    Tensor vector0 = Tensors.vector(1, 2, 0.25);
    Tensor res0 = geodesicIIR2.apply(vector0);
    assertEquals(res0, vector0);
    Tensor vector1 = Tensors.vector(4, 5, 0.5);
    Tensor res1 = geodesicIIR2.apply(vector1);
    Chop._10.requireClose(res1, Tensors.vector(2.593872261349412, 3.406127738650588, 0.375));
  }

  public void testLinear() {
    Geodesic geodesicInterface = new HsGeodesic(RnManifold.INSTANCE);
    Scalar alpha = RationalScalar.HALF;
    TensorUnaryOperator tensorUnaryOperator = new GeodesicIIR2(geodesicInterface, alpha);
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(10)), RealScalar.of(10));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(10)), RealScalar.of(10));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20)), RealScalar.of(15));
    {
      Tensor tensor = tensorUnaryOperator.apply(RealScalar.of(20));
      ExactTensorQ.require(tensor);
      assertEquals(tensor, RealScalar.of(20));
    }
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20.)), RealScalar.of(22.5));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20.)), RealScalar.of(22.5));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20.)), RealScalar.of(21.25));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20.)), RealScalar.of(20));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20.)), RealScalar.of(19.375));
  }

  public void testId2() {
    Scalar alpha = RealScalar.ONE;
    TensorUnaryOperator tuo1 = CausalFilter.of(() -> new GeodesicIIR2(RnGeodesic.INSTANCE, alpha));
    for (int k = 0; k < 10; ++k) {
      Tensor signal = UnitVector.of(10, k);
      assertEquals(signal, tuo1.apply(signal));
    }
  }

  public void testId() {
    Scalar alpha = RealScalar.ONE;
    TensorUnaryOperator tuo1 = CausalFilter.of(() -> new GeodesicIIR2(RnGeodesic.INSTANCE, alpha));
    TensorUnaryOperator tuo2 = GeodesicIIRnFilter.of( //
        GeodesicExtrapolation.of(RnGeodesic.INSTANCE, MonomialExtrapolationMask.INSTANCE), RnGeodesic.INSTANCE, 2, alpha);
    Tensor signal = RandomVariate.of(UniformDistribution.unit(), 10);
    Tensor r1 = tuo1.apply(signal);
    Tensor r2 = tuo2.apply(signal);
    Chop._10.requireClose(r1, r2);
  }
}
