// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

/** @see CoordinateBoundingBox */
@FunctionalInterface
public interface RegionBoundsInterface {
  /** @return box that contains region entirely */
  CoordinateBoundingBox coordinateBounds();
}
