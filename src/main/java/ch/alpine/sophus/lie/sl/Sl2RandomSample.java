// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.math.sample.DnRandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.pdf.Distribution;

public enum Sl2RandomSample {
  ;
  public static RandomSampleInterface of(Distribution xy, Distribution z) {
    return DnRandomSample.of(xy, xy, z);
  }
}
