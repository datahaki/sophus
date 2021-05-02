// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.math.BijectionFamily;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.DiscreteUniformDistribution;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class SimpleR2TranslationFamilyTest extends TestCase {
  public void testSimple() {
    BijectionFamily bijectionFamily = new SimpleR2TranslationFamily(s -> Tensors.of(RealScalar.of(3), RealScalar.of(100).add(s)));
    Distribution distribution = DiscreteUniformDistribution.of(-15, 16);
    for (int index = 0; index < 100; ++index) {
      Scalar scalar = RandomVariate.of(distribution);
      Tensor point = RandomVariate.of(distribution, 2);
      Tensor fwd = bijectionFamily.forward(scalar).apply(point);
      assertEquals(bijectionFamily.inverse(scalar).apply(fwd), point);
    }
  }
}
