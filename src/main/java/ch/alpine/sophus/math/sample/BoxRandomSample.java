// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.Box;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

public class BoxRandomSample implements RandomSampleInterface, Serializable {
  /** @param box axis-aligned bounding box
   * @return */
  public static RandomSampleInterface of(Box box) {
    return new BoxRandomSample(box);
  }

  /** the parameters define the coordinate bounds of the axis-aligned box
   * from which the samples are drawn
   * 
   * @param min lower-left
   * @param max upper-right
   * @see Box */
  public static RandomSampleInterface of(Tensor min, Tensor max) {
    return of(Box.of(min, max));
  }

  // ---
  private final List<Distribution> distributions = new LinkedList<>();

  private BoxRandomSample(Box box) {
    for (int index = 0; index < box.dimensions(); ++index)
      distributions.add(UniformDistribution.of(box.getClip(index)));
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Tensor.of(distributions.stream() //
        .map(distribution -> RandomVariate.of(distribution, random)));
  }
}
