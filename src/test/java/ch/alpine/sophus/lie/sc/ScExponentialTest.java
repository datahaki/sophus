// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class ScExponentialTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-500, 500);
    for (int count = 0; count < 100; ++count) {
      Tensor x = RandomVariate.of(distribution, 1);
      Tensor exp = ScExponential.INSTANCE.exp(x);
      Tensor log = ScExponential.INSTANCE.log(exp);
      Chop._10.requireClose(x, log);
    }
  }
}