// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import junit.framework.TestCase;

public class VariogramFunctionsTest extends TestCase {
  public void testSimple() {
    for (VariogramFunctions variograms : VariogramFunctions.values()) {
      ScalarUnaryOperator suo = variograms.of(RealScalar.ONE);
      suo.toString();
    }
  }
}
