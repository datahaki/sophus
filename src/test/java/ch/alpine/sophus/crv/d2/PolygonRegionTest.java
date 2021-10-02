// code by jph
package ch.alpine.sophus.crv.d2;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class PolygonRegionTest extends TestCase {
  public void testEmpty() throws ClassNotFoundException, IOException {
    PolygonRegion polygonRegion = Serialization.copy(new PolygonRegion(Tensors.of(Tensors.vector(1, 2))));
    polygonRegion.test(Tensors.vector(1, 2));
  }

  public void testFilter() {
    Optional<Tensor> optional = Stream.of(Array.zeros(2)).filter(new PolygonRegion(CirclePoints.of(3))).findAny();
    assertTrue(optional.isPresent());
  }

  public void testDimensionsFail() {
    AssertFail.of(() -> new PolygonRegion(Tensors.fromString("{{1,2,3}}")));
  }

  public void testInside() {
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0, 0 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    });
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.5, .5)));
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.9, .9)));
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.1, .1)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.1, -0.1)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(1, 1.1)));
  }

  public void testInsideQuantity() {
    ScalarUnaryOperator suo = s -> Quantity.of(s, "km");
    Tensor polygon = Tensors.matrix(new Number[][] { //
        { 0, 0 }, //
        { 1, 0 }, //
        { 1, 1 }, //
        { 0, 1 } //
    }).map(suo).unmodifiable();
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.5, .5).map(suo)));
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.9, .9).map(suo)));
    assertTrue(PolygonRegion.isInside(polygon, Tensors.vector(0.1, .1).map(suo)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.1, -0.1).map(suo)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(1, 1.1).map(suo)));
  }

  public void testInsideEmpty() {
    Tensor polygon = Tensors.empty();
    assertFalse(OriginEnclosureQ.INSTANCE.test(polygon));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.5, .5)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.9, .9)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.1, .1)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(0.1, -0.1)));
    assertFalse(PolygonRegion.isInside(polygon, Tensors.vector(1, 1.1)));
  }
}
