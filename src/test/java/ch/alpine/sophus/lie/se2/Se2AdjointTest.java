// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class Se2AdjointTest {
  @Test
  public void testRotationFixpointSideLeft() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.inverse(Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = se2Adjoint.apply(Tensors.vector(1, 0, 1)); // more forward and turn left
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // only rotation
  }

  @Test
  public void testRotationSideLeft() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(Tensors.vector(0, 1, 0)); // "left rear wheel"
    Tensor tensor = se2Adjoint.apply(Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, UnitVector.of(3, 2).negate()); // only rotation
  }

  @Test
  public void testRotationFixpointSideRight() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(Tensors.vector(0, -1, 0)); // "right rear wheel"
    Tensor tensor = se2Adjoint.apply(Tensors.vector(1, 0, -1)); // more forward and turn right
    Chop._13.requireClose(tensor, Tensors.vector(2, 0, -1)); // rotate and translate
  }

  @Test
  public void testRotationId() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(Tensors.vector(0, 0, 2));
    Tensor tensor = se2Adjoint.apply(Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, UnitVector.of(3, 2)); // same rotation
  }

  @Test
  public void testRotationTranslation() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(Tensors.vector(1, 0, Math.PI / 2));
    Tensor tensor = se2Adjoint.apply(Tensors.vector(0, 0, 1));
    Chop._13.requireClose(tensor, Tensors.vector(0, -1, 1));
  }

  @Test
  public void testTranslate() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(RandomVariate.of(distribution, 2).append(RealScalar.ZERO));
      Tensor uvw = RandomVariate.of(distribution, 2).append(RealScalar.ZERO);
      Tensor tensor = se2Adjoint.apply(uvw);
      Chop._13.requireClose(tensor, uvw); // only translation
    }
  }

  @Test
  public void testComparison() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(xya);
      Se2AdjointComp se2AdjointComp = new Se2AdjointComp(xya);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(se2Adjoint.apply(uvw), se2AdjointComp.apply(uvw)); // only translation
    }
  }

  @Test
  public void testFwdInv() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      TensorUnaryOperator forward = Se2Adjoint.forward(xya);
      TensorUnaryOperator inverse = Se2Adjoint.inverse(xya);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Chop._13.requireClose(inverse.apply(forward.apply(uvw)), uvw);
      Chop._13.requireClose(forward.apply(inverse.apply(uvw)), uvw);
    }
  }

  @Test
  public void testNonCovering() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor res = Se2Adjoint.forward(xya).apply(uvw);
      for (int v = -3; v <= 3; ++v) {
        Tensor xyp = xya.copy();
        xyp.set(RealScalar.of(v * 2 * Math.PI)::add, 2);
        Chop._13.requireClose(res, Se2Adjoint.forward(xyp).apply(uvw));
      }
    }
  }

  @Test
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(g);
      Tensor u_w = RandomVariate.of(distribution, 3);
      Tensor out = se2Adjoint.apply(u_w);
      assertEquals(Dimensions.of(out), Arrays.asList(3));
      Tensor g_i = new Se2GroupElement(g).inverse().combine(Array.zeros(3));
      TensorUnaryOperator se2Inverse = Se2Adjoint.forward(g_i);
      Tensor apply = se2Inverse.apply(out);
      Chop._13.requireClose(u_w, apply);
    }
  }

  @Test
  public void testUnits() {
    TensorUnaryOperator se2Adjoint = Se2Adjoint.forward(Tensors.fromString("{2[m], 3[m], 4}"));
    Tensor tensor = se2Adjoint.apply(Tensors.fromString("{7[m*s^-1], -5[m*s^-1], 1[s^-1]}"));
    Chop._13.requireClose(tensor, //
        Tensors.fromString("{-5.359517822584925[m*s^-1], -4.029399362837438[m*s^-1], 1[s^-1]}"));
  }

  @Test
  public void testFail() {
    AssertFail.of(() -> Se2Adjoint.forward(RealScalar.ONE));
    AssertFail.of(() -> Se2Adjoint.forward(HilbertMatrix.of(3)));
    AssertFail.of(() -> Se2Adjoint.inverse(RealScalar.ONE));
    AssertFail.of(() -> Se2Adjoint.inverse(HilbertMatrix.of(3)));
  }
}
