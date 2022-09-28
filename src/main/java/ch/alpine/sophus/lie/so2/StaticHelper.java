// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.MinMax;
import ch.alpine.tensor.sca.Clip;

/* package */ enum StaticHelper {
  ;
  /** @param sequence
   * @return given sequence
   * @throws Exception if span of entries exceeds or equals pi */
  public static Tensor rangeQ(Tensor sequence) {
    Clip clip = sequence.stream() //
        .map(Scalar.class::cast) //
        .collect(MinMax.toClip());
    if (Scalars.lessEquals(Pi.VALUE, clip.width()))
      throw new Throw(sequence);
    return sequence;
  }
}
