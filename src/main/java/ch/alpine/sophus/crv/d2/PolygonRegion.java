// code by jph
package ch.alpine.sophus.crv.d2;

import java.io.Serializable;

import ch.alpine.sophus.math.Region;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;

/** check if input tensor is inside a polygon in R^2 */
public class PolygonRegion implements Region<Tensor>, Serializable {
  private final CoordinateBoundingBox coordinateBoundingBox;
  private final Tensor polygon;

  /** @param polygon as matrix with dimensions n x 2 */
  public PolygonRegion(Tensor polygon) {
    Integers.requireEquals(Unprotect.dimension1Hint(polygon), 2);
    coordinateBoundingBox = CoordinateBounds.of(polygon);
    this.polygon = polygon;
  }

  @Override // from Region
  public boolean test(Tensor tensor) {
    // TODO SOPHUS ALG design strict: only valid input
    Tensor point = tensor.extract(0, 2);
    return coordinateBoundingBox.isInside(point) //
        && FranklinPnpoly.isInside(polygon, point);
  }

  public Tensor polygon() {
    return polygon.unmodifiable();
  }
}
