// code by jph
package ch.alpine.sophus.math.sample;

import java.util.stream.Collectors;

import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.pdf.c.UniformDistribution;

/** the parameters define the coordinate bounds of the axis-aligned box
 * from which the samples are drawn
 * 
 * @see CoordinateBoundingBox */
public enum BoxRandomSample {
  ;
  /** @param coordinateBoundingBox axis-aligned bounding box
   * @return
   * @see CoordinateBounds */
  public static RandomSampleInterface of(CoordinateBoundingBox coordinateBoundingBox) {
    return DnRandomSample.of(coordinateBoundingBox.stream() //
        .map(UniformDistribution::of) //
        .collect(Collectors.toList()));
  }
}
