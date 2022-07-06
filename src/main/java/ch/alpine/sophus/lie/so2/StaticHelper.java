// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.ScalarSummaryStatistics;

/* package */ enum StaticHelper {
  ;
  /** @param sequence
   * @return given sequence
   * @throws Exception if span of entries exceeds or equals pi */
  public static Tensor rangeQ(Tensor sequence) {
    ScalarSummaryStatistics scalarSummaryStatistics = sequence.stream() //
        .map(Scalar.class::cast) //
        .collect(ScalarSummaryStatistics.collector());
    if (Scalars.lessEquals(Pi.VALUE, scalarSummaryStatistics.getClip().width()))
      throw Throw.of(sequence);
    return sequence;
  }
}
