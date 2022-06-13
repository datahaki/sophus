// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.qty.Quantity;

class PolygonRegionTest {
  @Test
  void testEmpty() throws ClassNotFoundException, IOException {
    PolygonRegion polygonRegion = Serialization.copy(new PolygonRegion(Tensors.of(Tensors.vector(1, 2))));
    polygonRegion.test(Tensors.vector(1, 2));
  }

  @Test
  void testFilter() {
    Optional<Tensor> optional = Stream.of(Array.zeros(2)).filter(new PolygonRegion(CirclePoints.of(3))).findAny();
    assertTrue(optional.isPresent());
  }

  @Test
  void testDimensionsFail() {
    assertThrows(Exception.class, () -> new PolygonRegion(Tensors.fromString("{{1,2,3}}")));
  }

  @Test
  void testInside() {
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0, 0 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    });
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.5, .5)));
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.9, .9)));
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, .1)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, -0.1)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(1, 1.1)));
  }

  @Test
  void testInsideQuantity() {
    ScalarUnaryOperator suo = s -> Quantity.of(s, "km");
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0, 0 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    }).map(suo).unmodifiable();
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.5, .5).map(suo)));
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.9, .9).map(suo)));
    assertTrue(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, .1).map(suo)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, -0.1).map(suo)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(1, 1.1).map(suo)));
  }

  @Test
  void testInsideEmpty() {
    Tensor polygon = Tensors.empty();
    assertFalse(OriginEnclosureQ.INSTANCE.test(polygon));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.5, .5)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.9, .9)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, .1)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(0.1, -0.1)));
    assertFalse(FranklinPnpoly.isInside(polygon, Tensors.vector(1, 1.1)));
  }
}
