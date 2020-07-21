// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class StAdMetricTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);

  public void testSimple() {
    Tensor m = TestHelper.spawn_St1();
    StAdMetric stAdMetric = new StAdMetric(m);
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
