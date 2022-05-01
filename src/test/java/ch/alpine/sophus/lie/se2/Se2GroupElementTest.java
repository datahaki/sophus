// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class Se2GroupElementTest {
  @Test
  public void testSimple() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = element.combine(Tensors.vector(6, 7, 8));
    assertTrue(Sign.isNegative(tensor.Get(2)));
  }

  @Test
  public void testInverse() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(1, 2, 3));
    assertInstanceOf(Se2GroupElement.class, element.inverse());
  }

  @Test
  public void testRotationFixpointSideLeft() {
    Se2GroupElement element = (Se2GroupElement) Se2Group.INSTANCE.element(Tensors.vector(0, 1, 0)).inverse(); // "left rear wheel"
    Tensor tensor = element.adjoint(Tensors.vector(1, 0, 1)); // more forward and turn left
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // only rotation
  }

  @Test
  public void testRotationSideLeft() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = element.adjoint(Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, UnitVector.of(3, 2).negate()); // only rotation
  }

  @Test
  public void testRotationFixpointSideRight() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(0, -1, 0)); // "right rear wheel"
    Tensor tensor = element.adjoint(Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, Tensors.vector(2, 0, -1)); // rotate and translate
  }

  @Test
  public void testRotationId() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(0, 0, 2));
    Tensor tensor = element.adjoint(Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // same rotation
  }

  @Test
  public void testRotationTranslation() {
    Se2GroupElement element = Se2Group.INSTANCE.element(Tensors.vector(1, 0, Math.PI / 2));
    Tensor tensor = element.adjoint(Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, Tensors.vector(0, -1, 1));
  }

  @Test
  public void testTranslate() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Se2GroupElement element = Se2Group.INSTANCE.element(RandomVariate.of(distribution, 2).append(RealScalar.ZERO));
      Tensor uvw = RandomVariate.of(distribution, 2).append(RealScalar.ZERO);
      Tensor tensor = element.adjoint(uvw);
      Chop._13.requireClose(tensor, uvw); // only translation
    }
  }

  @Test
  public void testComparison() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2GroupElement element = Se2Group.INSTANCE.element(xya);
      Se2AdjointComp se2AdjointComp = new Se2AdjointComp(xya);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(element.adjoint(uvw), se2AdjointComp.apply(uvw)); // only translation
    }
  }

  @Test
  public void testFwdInv() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2GroupElement element = Se2Group.INSTANCE.element(xya);
      TensorUnaryOperator inverse = Se2Group.INSTANCE.element(xya).inverse()::adjoint;
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(inverse.apply(element.adjoint(uvw)), uvw);
      Chop._13.requireClose(element.adjoint(inverse.apply(uvw)), uvw);
    }
  }

  @Test
  public void testNonCovering() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor res = Se2Group.INSTANCE.element(xya).adjoint(uvw);
      for (int v = -3; v <= 3; ++v) {
        Tensor xyp = xya.copy();
        xyp.set(RealScalar.of(v * 2 * Math.PI)::add, 2);
        Chop._13.requireClose(res, Se2Group.INSTANCE.element(xyp).adjoint(uvw));
      }
    }
  }

  @Test
  public void testSimple2() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      TensorUnaryOperator se2Adjoint = Se2Group.INSTANCE.element(g)::adjoint;
      Tensor u_w = RandomVariate.of(distribution, 3);
      Tensor out = se2Adjoint.apply(u_w);
      assertEquals(Dimensions.of(out), Arrays.asList(3));
      Tensor g_i = new Se2GroupElement(g).inverse().combine(Array.zeros(3));
      TensorUnaryOperator se2Inverse = Se2Group.INSTANCE.element(g_i)::adjoint;
      Tensor apply = se2Inverse.apply(out);
      Chop._13.requireClose(u_w, apply);
    }
  }

  @Test
  public void testUnits() {
    TensorUnaryOperator se2Adjoint = Se2Group.INSTANCE.element(Tensors.fromString("{2[m], 3[m], 4}"))::adjoint;
    Tensor tensor = se2Adjoint.apply(Tensors.fromString("{7[m*s^-1], -5[m*s^-1], 1[s^-1]}"));
    Chop._13.requireClose(tensor, //
        Tensors.fromString("{-5.359517822584925[m*s^-1], -4.029399362837438[m*s^-1], 1[s^-1]}"));
  }

  @Test
  public void testLinearGroupSe2() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor adjoint = new Se2GroupElement(g).adjoint(uvw);
      Tensor gM = Se2Matrix.of(g);
      Tensor X = Tensors.matrix(new Scalar[][] { //
          { RealScalar.ZERO, uvw.Get(2).negate(), uvw.Get(0) }, //
          { uvw.Get(2), RealScalar.ZERO, uvw.Get(1) }, //
          { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO } });
      Tensor tensor = gM.dot(X).dot(Inverse.of(gM));
      Tensor xya = Tensors.of(tensor.Get(0, 2), tensor.Get(1, 2), tensor.Get(1, 0));
      Chop._12.requireClose(adjoint, xya);
    }
  }

  @Test
  public void testDL() {
    Se2GroupElement se2GroupElement = new Se2GroupElement(Tensors.vector(0, 0, Math.PI / 2));
    Tensor dL = se2GroupElement.dL(Tensors.vector(1, 0, 0));
    Chop._12.requireClose(dL, UnitVector.of(3, 1).negate());
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> Se2Adjoint.forward(RealScalar.ONE));
    assertThrows(Exception.class, () -> Se2Adjoint.forward(HilbertMatrix.of(3)));
    assertThrows(Exception.class, () -> Se2Adjoint.inverse(RealScalar.ONE));
    assertThrows(Exception.class, () -> Se2Adjoint.inverse(HilbertMatrix.of(3)));
  }

  @Test
  public void testDlNullFail() {
    Se2GroupElement se2GroupElement = new Se2GroupElement(Tensors.vector(0, 0, Math.PI / 2));
    assertThrows(Exception.class, () -> se2GroupElement.dL(null));
  }
}
