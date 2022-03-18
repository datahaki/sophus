// code by jph
package ch.alpine.sophus.decim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnCurveDecimation;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Sort;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;

public class RamerDouglasPeuckerTest {
  @Test
  public void testEmpty() {
    Tensor tensor = Tensors.empty();
    assertEquals(RnCurveDecimation.of(RealScalar.of(1)).apply(tensor), tensor);
  }

  @Test
  public void testPoints1() {
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 } });
    assertEquals(RnCurveDecimation.of(RealScalar.of(1)).apply(tensor), tensor);
  }

  @Test
  public void testPoints1copy() {
    Tensor origin = Tensors.matrix(new Number[][] { { 1, 1 } });
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 } });
    Tensor result = RnCurveDecimation.of(RealScalar.of(1)).apply(tensor);
    result.set(Scalar::zero, Tensor.ALL, Tensor.ALL);
    assertEquals(origin, tensor);
  }

  @Test
  public void testPoints2() {
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 }, { 5, 2 } });
    assertEquals(RnCurveDecimation.of(RealScalar.of(1)).apply(tensor), tensor);
  }

  @Test
  public void testPoints2same() {
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } });
    Tensor result = RnCurveDecimation.of(RealScalar.of(1)).apply(tensor);
    assertEquals(result, Tensors.fromString("{{1, 1}, {1, 1}}"));
  }

  @Test
  public void testPoints3a() {
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 }, { 1, 1 }, { 5, 2 } });
    Tensor result = RnCurveDecimation.of(RealScalar.of(0)).apply(tensor);
    assertEquals(result, Tensors.fromString("{{1, 1}, {5, 2}}"));
  }

  @Test
  public void testPoints3() {
    Tensor tensor = Tensors.matrix(new Number[][] { { 1, 1 }, { 3, 2 }, { 5, 2 } });
    assertEquals(RnCurveDecimation.of(RealScalar.of(1)).apply(tensor), //
        Tensors.matrixInt(new int[][] { { 1, 1 }, { 5, 2 } }));
    assertEquals(RnCurveDecimation.of(RealScalar.of(0.1)).apply(tensor), tensor);
  }

  @Test
  public void testRandom() {
    int n = 20;
    Tensor tensor = Tensors.vector(i -> Tensors.vector(i, RandomVariate.of(NormalDistribution.standard()).number().doubleValue()), n);
    Tensor result = RnCurveDecimation.of(RealScalar.of(1)).apply(tensor);
    Tensor column = result.get(Tensor.ALL, 0);
    assertEquals(column, Sort.of(column));
    assertTrue(column.length() < n);
  }

  @Test
  public void testQuantity() {
    Tensor points = CirclePoints.of(100).map(value -> Quantity.of(value, "m"));
    Tensor tensor = RnCurveDecimation.of(Quantity.of(0.03, "m")).apply(points);
    assertEquals(tensor.length(), 17);
  }

  @Test
  public void test3d() {
    Tensor tensor = RnCurveDecimation.of(RealScalar.of(0.1)).apply(IdentityMatrix.of(3));
    assertEquals(tensor, IdentityMatrix.of(3));
  }

  @Test
  public void testEpsilonFail() {
    AssertFail.of(() -> RnCurveDecimation.of(RealScalar.of(-.1)));
  }

  @Test
  public void testFail() {
    AssertFail.of(() -> RnCurveDecimation.of(RealScalar.of(0.1)).apply(Tensors.fromString("{{{1}, 2}, {{1}, 2}, {{1}, 2}}")));
    AssertFail.of(() -> RnCurveDecimation.of(RealScalar.of(0.1)).apply(Array.zeros(3, 3, 3)));
    AssertFail.of(() -> RnCurveDecimation.of(RealScalar.of(0.1)).apply(Array.zeros(3, 2, 4)));
  }
}
