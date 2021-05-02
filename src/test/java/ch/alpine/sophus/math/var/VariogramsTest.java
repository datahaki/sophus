// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import junit.framework.TestCase;

public class VariogramsTest extends TestCase {
  public void testSimple() {
    for (Variograms variograms : Variograms.values()) {
      ScalarUnaryOperator suo = variograms.of(RealScalar.ONE);
      suo.toString();
    }
  }
}
