// code by ob
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class So2ExponentialTest {
  @Test
  public void testSimple() {
    Distribution distribution = UniformDistribution.unit();
    Scalar alpha = RandomVariate.of(distribution);
    assertEquals(alpha, So2Exponential.INSTANCE.log(So2Exponential.INSTANCE.exp(alpha)));
  }
}
