// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import junit.framework.TestCase;

public class VariogramsTest extends TestCase {
  public void testSimple() {
    for (Variograms variograms : Variograms.values()) {
      ScalarUnaryOperator suo = variograms.of(RealScalar.ONE);
      System.out.println(suo.toString());
    }
  }
}
