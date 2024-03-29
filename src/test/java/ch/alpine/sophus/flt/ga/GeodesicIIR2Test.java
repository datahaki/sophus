// code by ob, jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.MonomialExtrapolationMask;
import ch.alpine.sophus.flt.CausalFilter;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class GeodesicIIR2Test {
  @Test
  void testSimple() {
    GeodesicSpace geodesicSpace = Se2Group.INSTANCE;
    Scalar alpha = RationalScalar.HALF;
    GeodesicIIR2 geodesicIIR2 = new GeodesicIIR2(geodesicSpace, alpha);
    Tensor vector0 = Tensors.vector(1, 2, 0.25);
    Tensor res0 = geodesicIIR2.apply(vector0);
    assertEquals(res0, vector0);
    Tensor vector1 = Tensors.vector(4, 5, 0.5);
    Tensor res1 = geodesicIIR2.apply(vector1);
    Chop._10.requireClose(res1, Tensors.vector(2.593872261349412, 3.406127738650588, 0.375));
  }

  @Test
  void testLinear() {
    GeodesicSpace geodesicSpace = RnGroup.INSTANCE;
    Scalar alpha = RationalScalar.HALF;
    TensorUnaryOperator tensorUnaryOperator = new GeodesicIIR2(geodesicSpace, alpha);
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

  @Test
  void testId2() {
    Scalar alpha = RealScalar.ONE;
    TensorUnaryOperator tuo1 = CausalFilter.of(() -> new GeodesicIIR2(RnGroup.INSTANCE, alpha));
    for (int k = 0; k < 10; ++k) {
      Tensor signal = UnitVector.of(10, k);
      assertEquals(signal, tuo1.apply(signal));
    }
  }

  @Test
  void testId() {
    Scalar alpha = RealScalar.ONE;
    TensorUnaryOperator tuo1 = CausalFilter.of(() -> new GeodesicIIR2(RnGroup.INSTANCE, alpha));
    TensorUnaryOperator tuo2 = GeodesicIIRnFilter.of( //
        GeodesicExtrapolation.of(RnGroup.INSTANCE, MonomialExtrapolationMask.INSTANCE), RnGroup.INSTANCE, 2, alpha);
    Tensor signal = RandomVariate.of(UniformDistribution.unit(), 10);
    Tensor r1 = tuo1.apply(signal);
    Tensor r2 = tuo2.apply(signal);
    Chop._10.requireClose(r1, r2);
  }
}
