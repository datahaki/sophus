// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.RotateLeft;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class PolygonClipTest {
  public static boolean equalsCycle(Tensor cycle1, Tensor cycle2) {
    if (cycle1.length() == cycle2.length())
      for (int index = 0; index < cycle2.length(); ++index)
        if (cycle1.get(0).equals(cycle2.get(index)))
          return cycle1.equals(RotateLeft.of(cycle2, index));
    return false;
  }

  @Test
  void testEmpty() {
    Tensor a = Tensors.empty();
    Tensor b = Tensors.empty();
    Tensor result = PolygonClip.of(a).apply(b);
    assertTrue(Tensors.isEmpty(result));
    assertThrows(Exception.class, () -> result.append(RealScalar.ZERO));
    assertTrue(Tensors.isEmpty(a));
    assertTrue(Tensors.isEmpty(b));
  }

  @Test
  void testEmptyNonEmpty() {
    Tensor a = Tensors.empty();
    Tensor b = RandomVariate.of(UniformDistribution.unit(), 10, 2);
    Tensor result = PolygonClip.of(a).apply(b);
    assertTrue(Tensors.isEmpty(result));
  }

  @Test
  void testSimple() {
    // {{2.0, 1.0}, {1.0, 1.0}, {1.0, 2.0}}
    // {{2.0, 1.0}, {1.0, 1.0}, {1.0, 1.5}, {1.3333333333333333, 1.6666666666666667}}
    {
      Tensor poly1 = Tensors.fromString("{{1, 1}, {2, 1}, {1, 2}}");
      Tensor poly2 = Tensors.fromString("{{1, 1}, {2, 1}, {2, 3}, {1, 2}}");
      Tensor result = PolygonClip.of(poly1).apply(poly2);
      assertTrue(equalsCycle(poly1, result));
    }
    {
      Tensor poly1 = Tensors.fromString("{{1, 1}, {2, 1}, {1, 2}}");
      Tensor poly2 = Tensors.fromString("{{0, 1}, {2, 1}, {2, 2}}");
      Tensor result = PolygonClip.of(poly1).apply(poly2);
      assertTrue(equalsCycle(result, Tensors.fromString("{{4/3, 5/3}, {1, 3/2}, {1, 1}, {2, 1}}")));
    }
  }

  @Test
  void testSingle() {
    Tensor subject = Tensors.of(Tensors.vector(0.5, 0));
    Tensor result = PolygonClip.of(CirclePoints.of(6)).apply(subject);
    assertEquals(result, subject);
  }

  @Test
  void testMore() {
    Tensor clipper = Tensors.fromString("{{0, 0}, {1, 0}, {1, 1}, {0, 1}}");
    Tensor subject = Tensors.fromString("{{0, 0}, {1, 0}, {1/2, 1/2}}");
    Tensor result = PolygonClip.of(clipper).apply(subject);
    Scalar area = PolygonArea.of(result);
    assertEquals(area, RationalScalar.of(1, 4));
    assertTrue(ExactScalarQ.of(area));
  }

  @Test
  void testMore2() {
    Tensor clip = Tensors.fromString("{{0, 0}, {1, 0}, {1, 1}, {0, 1}}");
    Tensor subj = Tensors.fromString("{{0, 0}, {2, 0}, {1/2, 1/2}}");
    Tensor result = PolygonClip.of(clip).apply(subj);
    assertTrue(equalsCycle(result, Tensors.fromString("{{0, 0}, {1, 0}, {1, 1/3}, {1/2, 1/2}}")));
    Scalar area = PolygonArea.of(result);
    assertTrue(ExactScalarQ.of(area));
    assertEquals(area, RationalScalar.of(1, 3));
  }

  @Test
  void testMore3() throws ClassNotFoundException, IOException {
    Tensor clip = Tensors.fromString("{{0, 0}, {1, 0}, {1, 1}, {0, 1}}");
    Tensor subj = Tensors.fromString("{{0, 0}, {2, 0}, {1/2, 1/2}}");
    Tensor result = Serialization.copy(PolygonClip.of(subj)).apply(clip);
    assertTrue(equalsCycle(result, Tensors.fromString("{{1/2, 1/2}, {0, 0}, {1, 0}, {1, 1/3}}")));
    Scalar area = PolygonArea.of(result);
    assertTrue(ExactScalarQ.of(area));
    assertEquals(area, RationalScalar.of(1, 3));
  }

  @Test
  void testSome() {
    Tensor circ = CirclePoints.of(7).multiply(RealScalar.of(4));
    Tensor poly = Tensors.fromString(
        "{{-5, 0}, {-5.667, -1.050}, {-3.683, -4.750}, {0.067, -5.533}, {1.183, -3.850}, {3.383, -2.183}, {1.683, -1.517}, {-2.283, -3.183}, {-2.933, -2.617}, {-2.317, -1.650}, {-3.4, -2.35}}");
    TensorUnaryOperator tensorUnaryOperator = PolygonClip.of(circ);
    Tensor result = tensorUnaryOperator.apply(poly);
    assertEquals(Dimensions.of(result), Arrays.asList(9, 2));
  }

  @Test
  void testRandom() {
    Tensor circ = CirclePoints.of(7);
    TensorUnaryOperator tensorUnaryOperator = PolygonClip.of(circ);
    for (int n = 3; n < 20; ++n) {
      Tensor poly = RandomVariate.of(NormalDistribution.standard(), n, 2);
      Tensor result = tensorUnaryOperator.apply(poly);
      if (0 < result.length()) {
        MatrixQ.require(result);
        assertEquals((int) Dimensions.of(result).get(1), 2);
      }
    }
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> PolygonClip.of(HilbertMatrix.of(2, 3)));
  }
}
