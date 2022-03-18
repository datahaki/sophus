// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Sign;

public class Se2CoveringGroupElementTest {
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  private static Tensor adjoint(LieGroupElement lieGroupElement) {
    return Tensor.of(IdentityMatrix.of(3).stream().map(lieGroupElement::adjoint));
  }

  @Test
  public void testCirc() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2CoveringGroupElement se2GroupAction = new Se2CoveringGroupElement(xya);
      Tensor other = RandomVariate.of(distribution, 3);
      Tensor result = se2GroupAction.combine(other);
      Tensor prod = Se2Matrix.of(xya).dot(Se2Matrix.of(other));
      Tensor matrix = Se2Matrix.of(result);
      Chop._10.requireClose(prod, matrix);
    }
  }

  @Test
  public void testInverse() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor result = new Se2CoveringGroupElement(xya).inverse().combine(Array.zeros(3));
      Tensor prod = Inverse.of(Se2Matrix.of(xya));
      Tensor matrix = Se2Matrix.of(result);
      Chop._10.requireClose(prod, matrix);
    }
  }

  @Test
  public void testInverseCirc() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2CoveringGroupElement se2GroupAction = new Se2CoveringGroupElement(xya);
      Tensor result = se2GroupAction.inverse().combine(Array.zeros(3));
      Tensor circ = se2GroupAction.combine(result);
      Chop._14.requireAllZero(circ);
    }
  }

  @Test
  public void testIntegrator() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2CoveringGroupElement se2GroupAction = new Se2CoveringGroupElement(xya);
      Tensor v = RandomVariate.of(distribution, 3);
      Tensor other = Se2CoveringExponential.INSTANCE.exp(v);
      Tensor result = se2GroupAction.combine(other);
      Tensor prod = Se2CoveringIntegrator.INSTANCE.spin(xya, v);
      Chop._10.requireClose(prod, result);
    }
  }

  @Test
  public void testQuantity() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.34}");
    Tensor oth = Tensors.fromString("{-.3[m], 0.8[m], -0.5}");
    Se2CoveringGroupElement se2GroupAction = new Se2CoveringGroupElement(xya);
    Tensor inverse = se2GroupAction.inverse().combine(xya.map(Scalar::zero));
    assertEquals(inverse, Tensors.fromString("{-1.6097288498099749[m], -1.552022238915878[m], -0.34}"));
    Tensor circ = se2GroupAction.combine(oth);
    assertEquals(circ, Tensors.fromString("{0.4503839266288446[m], 2.654157604780433[m], -0.15999999999999998}"));
  }

  @Test
  public void testMatrixAction() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int index = 0; index < 10; ++index) {
      Tensor xya1 = RandomVariate.of(distribution, 3);
      Tensor xya2 = RandomVariate.of(distribution, 3);
      Tensor xya3 = new Se2CoveringGroupElement(xya1).combine(xya2);
      Tensor xyam = Se2Matrix.of(xya1).dot(Se2Matrix.of(xya2));
      Chop._12.requireClose(Se2Matrix.of(xya3), xyam);
    }
  }

  @Test
  public void testNoWrap() {
    Se2CoveringGroupElement element = new Se2CoveringGroupElement(Tensors.vector(1, 2, 3));
    Tensor tensor = element.combine(Tensors.vector(6, 7, 8));
    assertTrue(Sign.isPositive(tensor.Get(2)));
  }

  @Test
  public void testInverseTensor() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.34}");
    Se2CoveringGroupElement element = new Se2CoveringGroupElement(xya);
    Tensor coordin = element.inverse().toCoordinate();
    Tensor combine = element.combine(coordin);
    Chop._12.requireClose(combine, Tensors.fromString("{0[m], 0[m], 0}"));
    element.dL(Tensors.fromString("{2[m*s^-1], 3[m*s^-1], 4[s^-1]}"));
    Tensor dR = element.dR(Tensors.fromString("{2[m*s^-1], 3[m*s^-1], 4[s^-1]}"));
    ExactTensorQ.require(dR);
  }

  @Test
  public void testAdjointCombine() {
    for (int count = 0; count < 10; ++count) {
      Tensor a = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      LieGroupElement ga = Se2CoveringGroup.INSTANCE.element(a);
      Tensor b = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      LieGroupElement gb = Se2CoveringGroup.INSTANCE.element(b);
      LieGroupElement gab = Se2CoveringGroup.INSTANCE.element(ga.combine(b));
      Tensor matrix = adjoint(gab);
      Tensor Ad_a = adjoint(ga);
      Tensor Ad_b = adjoint(gb);
      Tolerance.CHOP.requireClose(matrix, Ad_b.dot(Ad_a));
    }
  }

  @Test
  public void testDLNumeric() {
    Tensor g = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor x = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Scalar h = RealScalar.of(1e-6);
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(g);
    // Tensor gexphx =
    se2CoveringGroupElement.combine(Se2CoveringExponential.INSTANCE.exp(x.multiply(h)));
    // Tensor nu = gexphx.divide(h);
    // Tensor dL = se2CoveringGroupElement.dL(x);
    // System.out.println(nu);
    // System.out.println(dL);
  }

  @Test
  public void testDRDL_ad() {
    Tensor a = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    // LieGroupElement ga =
    Se2CoveringGroup.INSTANCE.element(a);
    // Tensor uvw = TestHelper.spawn_se2C();
    // Tensor lhs = ga.inverse().dR(ga.dL(uvw));
    // Tensor oth = ga.dL(ga.inverse().dR(uvw));
    // Tensor lh1 = ga.dR(ga.inverse().dL(uvw));
    // Tensor ot1 = ga.inverse().dL(ga.dR(uvw));
    // Tensor rhs = ga.adjoint(uvw);
    // System.out.println(lhs);
    // System.out.println(oth);
    // System.out.println(lh1);
    // System.out.println(ot1);
    // System.out.println(rhs);
    // Tolerance.CHOP.requireClose(lhs, rhs);
  }
}
