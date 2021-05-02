// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;

public class BoxRandomSample implements RandomSampleInterface, Serializable {
  /** the parameters define the coordinate bounds of the axis-aligned box
   * from which the samples are drawn
   * 
   * @param min lower-left
   * @param max upper-right */
  public static RandomSampleInterface of(Tensor min, Tensor max) {
    return new BoxRandomSample(min, max);
  }

  /***************************************************/
  private final List<Distribution> distributions = new LinkedList<>();

  private BoxRandomSample(Tensor min, Tensor max) {
    VectorQ.requireLength(min, max.length());
    for (int index = 0; index < min.length(); ++index)
      distributions.add(UniformDistribution.of(min.Get(index), max.Get(index)));
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Tensor.of(distributions.stream() //
        .map(distribution -> RandomVariate.of(distribution, random)));
  }
}
