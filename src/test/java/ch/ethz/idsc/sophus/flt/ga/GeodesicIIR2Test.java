// code by ob, jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.sophus.crv.spline.MonomialExtrapolationMask;
import ch.ethz.idsc.sophus.flt.CausalFilter;
import ch.ethz.idsc.sophus.lie.LieGroupGeodesic;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.sophus.lie.se2.Se2Group;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GeodesicIIR2Test extends TestCase {
  public void testSimple() {
    GeodesicInterface geodesicInterface = //
        new LieGroupGeodesic(Se2Group.INSTANCE, Se2CoveringExponential.INSTANCE);
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
    GeodesicInterface geodesicInterface = //
        new LieGroupGeodesic(RnGroup.INSTANCE, RnExponential.INSTANCE);
    Scalar alpha = RationalScalar.HALF;
    TensorUnaryOperator tensorUnaryOperator = new GeodesicIIR2(geodesicInterface, alpha);
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(10)), RealScalar.of(10));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(10)), RealScalar.of(10));
    assertEquals(tensorUnaryOperator.apply(RealScalar.of(20)), RealScalar.of(15));
    {
      Tensor tensor = tensorUnaryOperator.apply(RealScalar.of(20));
      ExactScalarQ.require(tensor.Get());
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
