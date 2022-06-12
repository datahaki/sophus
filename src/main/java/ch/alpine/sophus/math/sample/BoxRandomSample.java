// code by jph
package ch.alpine.sophus.math.sample;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.pdf.c.UniformDistribution;

public enum BoxRandomSample {
  ;
  /** @param coordinateBoundingBox axis-aligned bounding box
   * @return */
  public static RandomSampleInterface of(CoordinateBoundingBox coordinateBoundingBox) {
    return DnRandomSample.of(coordinateBoundingBox.stream() //
        .map(UniformDistribution::of) //
        .toList());
  }

  /** the parameters define the coordinate bounds of the axis-aligned box
   * from which the samples are drawn
   * 
   * @param min lower-left
   * @param max upper-right
   * @see CoordinateBoundingBox */
  public static RandomSampleInterface of(Tensor min, Tensor max) {
    return of(CoordinateBounds.of(min, max));
  }
}
