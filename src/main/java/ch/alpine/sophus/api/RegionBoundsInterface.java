// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

/** @see CoordinateBoundingBox */
@FunctionalInterface
public interface RegionBoundsInterface {
  /** @return box that contains region entirely */
  CoordinateBoundingBox coordinateBounds();
}
