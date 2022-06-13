// code by jph
package ch.alpine.sophus.crv.dub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class DubinsPathTest {
  @Test
  void testFirstTurnRight() {
    assertFalse(DubinsType.LSR.isFirstTurnRight());
    assertTrue(DubinsType.RSL.isFirstTurnRight());
    assertFalse(DubinsType.LSL.isFirstTurnRight());
    assertTrue(DubinsType.RSR.isFirstTurnRight());
    assertFalse(DubinsType.LRL.isFirstTurnRight());
    assertTrue(DubinsType.RLR.isFirstTurnRight());
  }

  @Test
  void testFirstEqualsLast() {
    assertFalse(DubinsType.LSR.isFirstEqualsLast());
    assertFalse(DubinsType.RSL.isFirstEqualsLast());
    assertTrue(DubinsType.LSL.isFirstEqualsLast());
    assertTrue(DubinsType.RSR.isFirstEqualsLast());
    assertTrue(DubinsType.LRL.isFirstEqualsLast());
    assertTrue(DubinsType.RLR.isFirstEqualsLast());
  }

  @Test
  void testContainsStraight() {
    assertTrue(DubinsType.LSR.containsStraight());
    assertTrue(DubinsType.RSL.containsStraight());
    assertTrue(DubinsType.LSL.containsStraight());
    assertTrue(DubinsType.RSR.containsStraight());
    assertFalse(DubinsType.LRL.containsStraight());
    assertFalse(DubinsType.RLR.containsStraight());
  }

  @Test
  void testTangentUnit() {
    Tensor tensor = DubinsType.LSR.tangent(2, Quantity.of(10, "m"));
    assertEquals(tensor.get(0), RealScalar.ONE);
    assertEquals(tensor.get(1), RealScalar.ZERO);
    assertEquals(tensor.get(2), Quantity.of(RationalScalar.of(-1, 10), "m^-1"));
  }

  @Test
  void testTangentDimensionless() {
    Tensor tensor = DubinsType.LSR.tangent(2, RealScalar.of(10));
    assertEquals(tensor.get(0), RealScalar.ONE);
    assertEquals(tensor.get(1), RealScalar.ZERO);
    assertEquals(tensor.get(2), RationalScalar.of(-1, 10));
  }

  @Test
  void testSignatureAbs() {
    assertEquals(DubinsType.LSL.signatureAbs(), DubinsType.LSR.signatureAbs());
    assertEquals(DubinsType.LSL.signatureAbs(), DubinsType.LSR.signatureAbs());
    assertEquals(DubinsType.LSL.signatureAbs(), DubinsType.RSR.signatureAbs());
    assertEquals(DubinsType.LSL.signatureAbs(), DubinsType.RSL.signatureAbs());
    assertEquals(DubinsType.LSL.signatureAbs(), Tensors.vector(1, 0, 1));
    assertEquals(DubinsType.LRL.signatureAbs(), DubinsType.RLR.signatureAbs());
    assertEquals(DubinsType.LRL.signatureAbs(), Tensors.vector(1, 1, 1));
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> DubinsType.LSR.tangent(2, RealScalar.of(-10)));
  }

  @Test
  void testAnticipateFail() {
    assertThrows(Exception.class, () -> DubinsType.LSR.tangent(-2, RealScalar.of(10)));
  }

  @Test
  void testWithoutUnits() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LSR, RealScalar.of(2), Tensors.vector(3, 2, 1));
    ScalarTensorFunction scalarTensorFunction = dubinsPath.sampler(Tensors.vector(0, 0, 0));
    Chop._10.requireClose(scalarTensorFunction.apply(RealScalar.of(0.3)), //
        Tensors.fromString("{0.29887626494719843, 0.022457844127915516, 0.15}"));
    Chop._10.requireClose(scalarTensorFunction.apply(RealScalar.of(4.7)), //
        Tensors.fromString("{2.1152432160432038, 3.554267073891487, 1.5}"));
    Chop._10.requireClose(scalarTensorFunction.apply(RealScalar.of(5.8)), //
        Tensors.fromString("{2.349039629628753, 4.6192334093884515, 1.1}"));
  }

  @Test
  void testUnits() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LRL, Quantity.of(2, "m"), Tensors.fromString("{1[m], 10[m], 1[m]}"));
    assertEquals(dubinsPath.length(), Quantity.of(12, "m"));
    ScalarTensorFunction scalarTensorFunction = dubinsPath.sampler(Tensors.fromString("{1[m], 2[m], 3}"));
    Tensor tensor = scalarTensorFunction.apply(Quantity.of(0.3, "m"));
    Chop._10.requireClose(tensor, Tensors.fromString("{0.7009454891459682[m], 2.0199443237417927[m], 3.15}"));
  }

  @Test
  void testMemberFuncs() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LRL, Quantity.of(2, "m"), Tensors.fromString("{1[m], 10[m], 1[m]}"));
    assertEquals(dubinsPath.type(), DubinsType.LRL);
    Scalar curvature = dubinsPath.totalCurvature();
    ExactScalarQ.require(curvature);
    assertEquals(curvature, RealScalar.of(6));
  }

  @Test
  void testStraight() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LSL, Quantity.of(2, "m"), Tensors.fromString("{0[m], 10[m], 0[m]}"));
    assertEquals(dubinsPath.totalCurvature(), RealScalar.ZERO);
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    DubinsPath path = DubinsPath.of(DubinsType.LRL, Quantity.of(2, "m"), Tensors.fromString("{1[m], 8[m], 1[m]}"));
    DubinsPath dubinsPath = Serialization.copy(path);
    assertEquals(dubinsPath.type(), DubinsType.LRL);
    assertEquals(dubinsPath.length(), Quantity.of(10, "m"));
    ScalarTensorFunction scalarTensorFunction = dubinsPath.sampler(Tensors.fromString("{1[m], 2[m], 3}"));
    Tensor tensor = scalarTensorFunction.apply(Quantity.of(0.3, "m"));
    Chop._10.requireClose(tensor, Tensors.fromString("{0.7009454891459682[m], 2.0199443237417927[m], 3.15}"));
  }

  @Test
  void testSignFail() {
    assertThrows(Exception.class, () -> DubinsPath.of(DubinsType.LRL, RealScalar.ONE, Tensors.vector(1, -10, 1)));
  }

  @Test
  void testOutsideFail() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LRL, RealScalar.ONE, Tensors.vector(1, 10, 1));
    assertEquals(dubinsPath.length(), Quantity.of(12, ""));
    ScalarTensorFunction scalarTensorFunction = dubinsPath.sampler(Tensors.vector(1, 2, 3));
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(RealScalar.of(-.1)));
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(RealScalar.of(13)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> DubinsPath.of(null, RealScalar.ONE, Tensors.vector(1, 10, 1)));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> DubinsPath.of(DubinsType.RLR, RealScalar.ONE, Tensors.vector(1, 10, 1, 3)));
  }
}
