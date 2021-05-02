// code by jph
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class DtAdMetricTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(DtGroup.INSTANCE);

  public void testSimple() {
    Tensor m = TestHelper.spawn_St1();
    DtAdMetric stAdMetric = new DtAdMetric(m);
    Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St1(), 5);
    // Tensor d1 = stAdMetric.all(sequence, m);
    Tensor shift = TestHelper.spawn_St1();
    for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
      Tensor seqL = tensorMapping.slash(sequence);
      Tensor mL = tensorMapping.apply(m);
      stAdMetric.all(seqL, mL);
    }
  }
}
