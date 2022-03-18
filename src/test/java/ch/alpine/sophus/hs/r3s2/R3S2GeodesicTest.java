// code by jph
package ch.alpine.sophus.hs.r3s2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

public class R3S2GeodesicTest {
  @Test
  public void testZero() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"), //
        Tensors.fromString("{{8, 8, 8}, {0, 1, 0}}"), //
        RealScalar.ZERO);
    assertEquals(split, Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"));
  }

  @Test
  public void testCurve() {
    Tensor p = Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}");
    Tensor q = Tensors.fromString("{{8, 8, 8}, {0, 1, 0}}");
    ScalarTensorFunction curve = R3S2Geodesic.INSTANCE.curve(p, q);
    Tensor domain = Subdivide.of(0, 1, 12);
    Tensor result = domain.map(curve);
    Tensor splits = domain.map(t -> R3S2Geodesic.INSTANCE.split(p, q, t));
    Chop._13.requireClose(result, splits);
  }

  @Test
  public void testOne() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"), //
        Tensors.fromString("{{8, 8, 8}, {0, 1, 0}}"), //
        RealScalar.ONE);
    Chop._10.requireClose(split, Tensors.fromString("{{8, 8, 8}, {0, 1, 0}}"));
  }

  @Test
  public void testHalfShift() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"), //
        Tensors.fromString("{{8, 8, 8}, {1, 0, 0}}"), //
        RationalScalar.HALF);
    Chop._10.requireClose(split, Tensors.fromString("{{4.5, 5, 5.5}, {1, 0, 0}}"));
  }

  @Test
  public void testHalfRotate() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"), //
        Tensors.fromString("{{1, 2, 3}, {0, 1, 0}}"), //
        RationalScalar.HALF);
    Chop._10.requireClose(split, Tensors.fromString( //
        "{{1, 2, 3}, {0.7071067811865476, 0.7071067811865475, 0}}"));
  }

  @Test
  public void testHalfSome() {
    Tensor split = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{1, 2, 3}, {1, 0, 0}}"), //
        Tensors.fromString("{{8, 8, 8}, {0, 1, 0}}"), //
        RationalScalar.HALF);
    Chop._10.requireClose(split.get(0), Tensors.fromString( //
        "{5.742640687119285, 3.5502525316941673, 5.5}"));
    Chop._10.requireClose(split.get(1), Tensors.fromString( //
        "{0.7071067811865476, 0.7071067811865475, 0}"));
  }

  @Test
  public void testSphereRepro() {
    Tensor n0 = UnitVector.of(3, 0);
    Tensor n1 = UnitVector.of(3, 1);
    Tensor p = Tensors.of(n0, n0);
    Tensor q = Tensors.of(n1, n1);
    Tensor split = R3S2Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    Chop._12.requireClose(split.get(0), split.get(1));
  }
}
