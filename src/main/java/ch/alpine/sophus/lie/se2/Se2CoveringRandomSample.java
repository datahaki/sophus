// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.math.sample.DnRandomSample;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;

public enum Se2CoveringRandomSample {
  ;
  /** @param distribution
   * @return */
  public static RandomSampleInterface uniform(Distribution distribution) {
    return DnRandomSample.of(distribution, distribution, distribution);
  }
}
