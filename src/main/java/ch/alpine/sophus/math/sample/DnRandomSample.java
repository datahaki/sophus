// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.List;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class DnRandomSample implements RandomSampleInterface, Serializable {
  public static RandomSampleInterface of(Distribution... distributions) {
    return new DnRandomSample(List.of(distributions));
  }

  public static RandomSampleInterface of(List<Distribution> list) {
    return new DnRandomSample(list);
  }

  // ---
  private final List<Distribution> list;

  private DnRandomSample(List<Distribution> list) {
    this.list = list;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return Tensor.of(list.stream() //
        .map(distribution -> RandomVariate.of(distribution, randomGenerator)));
  }
}
