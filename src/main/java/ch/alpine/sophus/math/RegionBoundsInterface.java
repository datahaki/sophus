// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

@FunctionalInterface
public interface RegionBoundsInterface {
  CoordinateBoundingBox coordinateBounds();
}
