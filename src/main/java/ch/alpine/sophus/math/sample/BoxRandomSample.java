// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.NdBox;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

public class BoxRandomSample implements RandomSampleInterface, Serializable {
  /** @param ndBox axis-aligned bounding box
   * @return */
  public static RandomSampleInterface of(NdBox ndBox) {
    return new BoxRandomSample(ndBox);
  }

  /** the parameters define the coordinate bounds of the axis-aligned box
   * from which the samples are drawn
   * 
   * @param min lower-left
   * @param max upper-right 
   * @see NdBox */
  public static RandomSampleInterface of(Tensor min, Tensor max) {
    return of(NdBox.of(min, max));
  }

  /***************************************************/
  private final List<Distribution> distributions = new LinkedList<>();

  private BoxRandomSample(NdBox ndBox) {
    for (int index = 0; index < ndBox.dimensions(); ++index)
      distributions.add(UniformDistribution.of(ndBox.clip(index)));
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Tensor.of(distributions.stream() //
        .map(distribution -> RandomVariate.of(distribution, random)));
  }
}
