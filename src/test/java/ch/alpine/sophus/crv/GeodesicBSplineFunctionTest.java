// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.itp.BSplineFunctionString;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class GeodesicBSplineFunctionTest {
  @Test
  void testLetsSee() {
    Distribution distribution = DiscreteUniformDistribution.of(-5, 5);
    for (int n = 1; n < 10; ++n)
      for (int degree = 0; degree < 6; ++degree) {
        Tensor control = RandomVariate.of(distribution, n, 3);
        ScalarTensorFunction stf1 = BSplineFunctionString.of(degree, control);
        ScalarTensorFunction stf2 = GeodesicBSplineFunction.of(RnGroup.INSTANCE, degree, control);
        Scalar x1 = RandomVariate.of(UniformDistribution.of(0, n - 1));
        Tensor y1 = stf1.apply(x1);
        Tensor y2 = stf2.apply(x1);
        Tolerance.CHOP.requireClose(y1, y2);
      }
  }

  @Test
  void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    int n = 20;
    Tensor domain = Subdivide.of(0, n - 1, 100);
    for (int degree = 1; degree < 7; ++degree) {
      Tensor control = RandomVariate.of(distribution, n, 3);
      GeodesicBSplineFunction mapForward = //
          GeodesicBSplineFunction.of(Se2CoveringGroup.INSTANCE, degree, control);
      Tensor forward = domain.map(mapForward);
      GeodesicBSplineFunction mapReverse = //
          GeodesicBSplineFunction.of(Se2CoveringGroup.INSTANCE, degree, Reverse.of(control));
      Tensor reverse = Reverse.of(domain.map(mapReverse));
      Chop._10.requireClose(forward, reverse);
      assertEquals(mapReverse.domain().min(), RealScalar.ZERO);
      assertEquals(mapReverse.domain().max(), RealScalar.of(19));
    }
  }

  @Test
  void testBasisWeights1a() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 1, UnitVector.of(3, 1));
    Tensor limitMask = Range.of(1, 2).map(func);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1}"));
  }

  @Test
  void testBasisWeights2() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 2, UnitVector.of(5, 2));
    Tensor limitMask = Range.of(1, 4).map(func);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/8, 3/4, 1/8}"));
  }

  @Test
  void testBasisWeights3a() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 3, UnitVector.of(7, 3));
    Tensor limitMask = Range.of(2, 5).map(func);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/6, 2/3, 1/6}"));
  }

  @Test
  void testBasisWeights3b() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 3, UnitVector.of(5, 2));
    Tensor limitMask = Range.of(1, 4).map(func);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/6, 2/3, 1/6}"));
  }

  @Test
  void testBasisWeights4() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 4, UnitVector.of(9, 4));
    Tensor limitMask = Range.of(2, 7).map(func);
    assertEquals(Total.of(limitMask), RealScalar.ONE);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/384, 19/96, 115/192, 19/96, 1/384}"));
  }

  @Test
  void testBasisWeights5a() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 5, UnitVector.of(11, 5));
    Tensor limitMask = Range.of(3, 8).map(func);
    assertEquals(Total.of(limitMask), RealScalar.ONE);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/120, 13/60, 11/20, 13/60, 1/120}"));
  }

  @Test
  void testBasisWeights5b() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 5, UnitVector.of(9, 4));
    Tensor limitMask = Range.of(2, 7).map(func);
    assertEquals(Total.of(limitMask), RealScalar.ONE);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/120, 13/60, 11/20, 13/60, 1/120}"));
  }

  @Test
  void testBasisWeights5c() {
    GeodesicBSplineFunction func = GeodesicBSplineFunction.of(RnGroup.INSTANCE, 5, UnitVector.of(7, 3));
    Tensor limitMask = Range.of(1, 6).map(func);
    assertEquals(Total.of(limitMask), RealScalar.ONE);
    ExactTensorQ.require(limitMask);
    assertEquals(limitMask, Tensors.fromString("{1/120, 13/60, 11/20, 13/60, 1/120}"));
  }

  @Test
  void testNonUniformKnots() {
    Tensor control = RandomVariate.of(DiscreteUniformDistribution.of(2, 102), 10, 4);
    Tensor domain = RandomVariate.of(UniformDistribution.of(0, 9), 100);
    for (int degree = 1; degree < 6; ++degree) {
      Tensor result = domain.map(GeodesicBSplineFunction.of(RnGroup.INSTANCE, degree, control));
      {
        Tensor vector = Range.of(5, 5 + control.length());
        Tensor compar = domain.map(RealScalar.of(5)::add) //
            .map(GeodesicBSplineFunction.of(RnGroup.INSTANCE, degree, vector, control));
        Tolerance.CHOP.requireClose(result, compar);
      }
      {
        Tensor vector = Range.of(5, 5 + control.length()).map(RealScalar.of(2)::multiply);
        Tensor compar = domain.map(RealScalar.of(5)::add).map(RealScalar.of(2)::multiply) //
            .map(GeodesicBSplineFunction.of(RnGroup.INSTANCE, degree, vector, control));
        Tolerance.CHOP.requireClose(result, compar);
      }
    }
  }

  @Test
  void testSasdlkjh() {
    {
      Tensor knots = Tensors.vector(1, 2, 3, 4, 5);
      GeodesicBSplineFunction.of(RnGroup.INSTANCE, 1, knots, knots);
    }
    Tensor knots = Tensors.vector(1, 2, 3, 2, 5);
    assertThrows(Exception.class, () -> GeodesicBSplineFunction.of(RnGroup.INSTANCE, 1, knots, knots));
  }

  @Test
  void testDegreeFail() {
    assertThrows(Exception.class, () -> GeodesicBSplineFunction.of(RnGroup.INSTANCE, -1, UnitVector.of(7, 3)));
  }

  @Test
  void testKnotsFail() {
    assertThrows(Exception.class, () -> GeodesicBSplineFunction.of(RnGroup.INSTANCE, 3, Range.of(0, 10), Range.of(0, 11)));
  }

  @Test
  void testKnotsUnsortedFail() {
    assertThrows(Exception.class, () -> GeodesicBSplineFunction.of(RnGroup.INSTANCE, 3, Tensors.vector(3, 2, 1), Tensors.vector(1, 2, 3)));
  }
}
