// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Total;

class AveragingWeightsTest {
  @RepeatedTest(7)
  void testSimple(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Tensor weights = AveragingWeights.of(n);
    assertEquals(weights.length(), n);
    assertEquals(Total.ofVector(weights), RealScalar.ONE);
    assertEquals(weights.get(0), Rational.of(1, n));
  }
}
