// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnExponentialTest {
  @Test
  void test2D() throws ClassNotFoundException, IOException {
    SnExponential snExp = Serialization.copy(new SnExponential(UnitVector.of(2, 0)));
    Scalar dist = RealScalar.of(0.2);
    Tensor log = snExp.log(AngleVector.of(dist));
    Chop._12.requireClose(log, UnitVector.of(2, 1).multiply(dist));
  }

  @Test
  void test2DNormFail() {
    assertThrows(Exception.class, () -> new SnExponential(Tensors.vector(2, 1)));
  }

  @Test
  void test2DExpFail() {
    SnExponential snExp = new SnExponential(UnitVector.of(2, 0));
    Scalar dist = RealScalar.of(0.2);
    assertThrows(Exception.class, () -> snExp.exp(AngleVector.of(dist)));
  }

  @Test
  void test3D() {
    Tensor tensor = new SnExponential(UnitVector.of(3, 0)).exp(UnitVector.of(3, 1).multiply(RealScalar.of(Math.PI / 2)));
    Chop._12.requireClose(tensor, UnitVector.of(3, 1));
  }

  @Test
  void test4D() {
    Tensor tensor = new SnExponential(UnitVector.of(4, 0)).exp(UnitVector.of(4, 1).multiply(RealScalar.of(Math.PI)));
    Chop._12.requireClose(tensor, UnitVector.of(4, 0).negate());
  }

  @Test
  void testId() {
    for (int dim = 2; dim < 6; ++dim)
      for (int count = 0; count < 20; ++count) {
        Tensor point = Vector2Norm.NORMALIZE.apply(RandomVariate.of(NormalDistribution.standard(), dim));
        Tensor apply = new SnExponential(point).exp(point.map(Scalar::zero));
        Tolerance.CHOP.requireClose(point, apply);
      }
  }

  @Test
  void testLog() {
    Tensor point = UnitVector.of(3, 0);
    SnExponential snExp = new SnExponential(point);
    Tensor g = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1));
    Tensor vector = snExp.log(g);
    Tensor retr = snExp.exp(vector);
    Chop._10.requireClose(g, retr);
  }

  @Test
  void test0Fail() {
    assertThrows(Exception.class, () -> new SnExponential(Tensors.empty()));
  }

  @Test
  void testDim0Len1() {
    Tensor p = UnitVector.of(1, 0);
    SnExponential snExponential = new SnExponential(p);
    snExponential.exp(Array.zeros(1));
    Tensor v = snExponential.log(p);
    assertEquals(v, Array.zeros(1));
  }

  @Test
  void testMatrixFail() {
    assertThrows(Exception.class, () -> new SnExponential(HilbertMatrix.of(3)));
  }

  @Test
  void testLogMemberFail() {
    SnExponential snExponential = new SnExponential(UnitVector.of(3, 0));
    assertThrows(Exception.class, () -> snExponential.log(Tensors.vector(1, 2, 3)));
  }
}
