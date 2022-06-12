// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.sophus.math.sample.DnRandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.pdf.Distribution;

public enum Se2CoveringRandomSample {
  ;
  /** @param distribution
   * @return */
  public static RandomSampleInterface uniform(Distribution distribution) {
    return DnRandomSample.of(distribution, distribution, distribution);
  }
}
