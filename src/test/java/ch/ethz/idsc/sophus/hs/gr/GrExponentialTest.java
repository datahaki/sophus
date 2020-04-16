// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class GrExponentialTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.fromString("{{0, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    Tensor v = Tensors.fromString("{{0, 0.2}, {-0.2, 0}}");
    Tensor exp = grExponential.exp(v);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
  }

  public void testPoint() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    Tensor v = Tensors.fromString("{{0, 0.2}, {-0.2, 0}}");
    Tensor exp = grExponential.exp(v);
    Tolerance.CHOP.requireClose(x, exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireAllZero(log);
  }
}
