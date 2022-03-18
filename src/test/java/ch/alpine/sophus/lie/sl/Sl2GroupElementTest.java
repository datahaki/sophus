// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

public class Sl2GroupElementTest {
  private static Tensor adjoint(LieGroupElement lieGroupElement) {
    return Tensor.of(IdentityMatrix.of(3).stream().map(lieGroupElement::adjoint));
  }

  @Test
  public void testSimple() {
    Tensor vector = Tensors.vector(-2, 7, 3);
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(vector);
    Sl2GroupElement inverse = sl2GroupElement.inverse();
    assertEquals(inverse.toCoordinate(), Tensors.fromString("{2/3, -7/3, 1/3}"));
    assertEquals(inverse.combine(vector), UnitVector.of(3, 2));
  }

  @Test
  public void testAdjointId() {
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(Tensors.vector(0, 0, 1));
    Tensor matrix = adjoint(sl2GroupElement);
    ExactTensorQ.require(matrix);
    assertEquals(matrix, IdentityMatrix.of(3));
  }

  @Test
  public void testAdjointCombine() {
    Random random = new Random(3);
    RandomSampleInterface rsi = new Sl2RandomSample( //
        DiscreteUniformDistribution.of(-10, 11), //
        DiscreteUniformDistribution.of(1, 10));
    for (int count = 0; count < 10; ++count) {
      Tensor a = RandomSample.of(rsi, random);
      VectorQ.require(a);
      Sl2GroupElement ga = Sl2Group.INSTANCE.element(a);
      Tensor b = RandomSample.of(rsi, random);
      Sl2GroupElement gb = Sl2Group.INSTANCE.element(b);
      Sl2GroupElement gab = Sl2Group.INSTANCE.element(ga.combine(b));
      Tensor matrix = adjoint(gab);
      ExactTensorQ.require(matrix);
      Tensor Ad_a = adjoint(ga);
      Tensor Ad_b = adjoint(gb);
      assertEquals(matrix, Ad_b.dot(Ad_a));
    }
  }

  @Test
  public void testFailZero() {
    AssertFail.of(() -> Sl2Group.INSTANCE.element(Tensors.vector(1, 2, 0)));
  }
}
