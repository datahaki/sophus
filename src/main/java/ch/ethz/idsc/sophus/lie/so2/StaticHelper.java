// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.math.ClipCover;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.red.ScalarSummaryStatistics;

/* package */ enum StaticHelper {
  ;
  /** @param sequence
   * @return given sequence
   * @throws Exception if span of entries exceeds or equals pi */
  public static Tensor rangeQ(Tensor sequence) {
    ScalarSummaryStatistics scalarSummaryStatistics = sequence.stream() //
        .map(Scalar.class::cast) //
        .collect(ScalarSummaryStatistics.collector());
    if (Scalars.lessEquals(Pi.VALUE, ClipCover.of(scalarSummaryStatistics).width()))
      throw TensorRuntimeException.of(sequence);
    return sequence;
  }
}
