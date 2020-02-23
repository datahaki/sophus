// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class StAdMetricTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);

  public void testSimple() {
    Tensor m = TestHelper.spawn_St1();
    StAdMetric stAdMetric = new StAdMetric(m);
    Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St1(), 5);
    Tensor d1 = stAdMetric.all(sequence, m);
    Tensor shift = TestHelper.spawn_St1();
    Tensor seqL = LIE_GROUP_OPS.allL(sequence, shift);
    Tensor mL = LIE_GROUP_OPS.combine(shift, m);
    Tensor dL = stAdMetric.all(seqL, mL);
    Tensor seqR = LIE_GROUP_OPS.allR(sequence, shift);
    Tensor mR = LIE_GROUP_OPS.combine(m, shift);
    Tensor dR = stAdMetric.all(seqR, mR);
    // Scalar dL = StAdMetric.INSTANCE.distance(StGroup.INSTANCE.element(s).combine(m), StGroup.INSTANCE.element(s).combine(x));
    // Scalar dR = StAdMetric.INSTANCE.distance(StGroup.INSTANCE.element(m).combine(s), StGroup.INSTANCE.element(x).combine(s));
    // System.out.println(d1);
    // System.out.println(dL.pmul(d1.map(Scalar::reciprocal)));
    // System.out.println(dR.pmul(d1.map(Scalar::reciprocal)));
    // System.out.println(dR);
  }
}
