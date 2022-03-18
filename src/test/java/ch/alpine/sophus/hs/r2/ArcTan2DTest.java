// code by jph
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.VectorAngle;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.ArcTan;

public class ArcTan2DTest {
  @Test
  public void testZero() {
    assertEquals(ArcTan2D.of(Array.zeros(10)), RealScalar.ZERO);
  }

  @Test
  public void testVectorXY() {
    assertEquals(ArcTan2D.of(Tensors.vector(-1, -2)), ArcTan.of(-1, -2));
    assertEquals(ArcTan2D.of(Tensors.vector(-1, -2, 3)), ArcTan.of(-1, -2));
  }

  @Test
  public void testVectorAngle() {
    Distribution distribution = UniformDistribution.of(-1, 1);
    Tensor v = UnitVector.of(2, 0);
    for (int count = 0; count < 10; ++count) {
      Tensor u = RandomVariate.of(distribution, 2);
      Optional<Scalar> optional = VectorAngle.of(u, v);
      Scalar scalar = ArcTan2D.of(u);
      Chop._10.requireClose(Abs.FUNCTION.apply(scalar), optional.get());
    }
  }

  @Test
  public void testComplex() {
    Scalar scalar = ArcTan2D.of(Tensors.fromString("{1 + I, 2 - 3*I}"));
    Scalar expect = ComplexScalar.of(-1.4808695768986575, -0.4023594781085251);
    Chop._12.requireClose(scalar, expect);
  }

  @Test
  public void testVectorXYFail() {
    AssertFail.of(() -> ArcTan2D.of(RealScalar.ZERO));
    AssertFail.of(() -> ArcTan2D.of(Tensors.vector(1)));
    AssertFail.of(() -> ArcTan2D.of(Array.zeros(3, 3, 3)));
  }
}
