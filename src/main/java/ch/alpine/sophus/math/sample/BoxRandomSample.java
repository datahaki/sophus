// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

public class BoxRandomSample implements RandomSampleInterface, Serializable {
  /** @param coordinateBoundingBox axis-aligned bounding box
   * @return */
  public static RandomSampleInterface of(CoordinateBoundingBox coordinateBoundingBox) {
    return new BoxRandomSample(coordinateBoundingBox);
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

  // ---
  private final List<Distribution> distributions = new LinkedList<>();

  private BoxRandomSample(CoordinateBoundingBox coordinateBoundingBox) {
    for (int index = 0; index < coordinateBoundingBox.dimensions(); ++index)
      distributions.add(UniformDistribution.of(coordinateBoundingBox.getClip(index)));
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Tensor.of(distributions.stream() //
        .map(distribution -> RandomVariate.of(distribution, random)));
  }
}
