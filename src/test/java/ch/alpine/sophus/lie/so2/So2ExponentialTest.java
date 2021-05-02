// code by ob
package ch.alpine.sophus.lie.so2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class So2ExponentialTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.unit();
    Scalar alpha = RandomVariate.of(distribution);
    assertEquals(alpha, So2Exponential.INSTANCE.log(So2Exponential.INSTANCE.exp(alpha)));
  }
}
