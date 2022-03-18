// code by jph
package ch.alpine.sophus.math.var;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public class VariogramFunctionsTest {
  @Test
  public void testSimple() {
    for (VariogramFunctions variograms : VariogramFunctions.values()) {
      ScalarUnaryOperator suo = variograms.of(RealScalar.ONE);
      suo.toString();
    }
  }
}
