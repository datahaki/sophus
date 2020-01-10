// code by jph
package ch.ethz.idsc.sophus.math.sample;

import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

public enum UniformRandomSample {
  ;
  public static RandomSampleInterface of(Distribution distribution, int length) {
    return random -> RandomVariate.of(distribution, random, length);
  }
}
