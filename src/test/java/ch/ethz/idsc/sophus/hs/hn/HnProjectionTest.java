// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class HnProjectionTest extends TestCase {
  public void testSimple() {
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 2, 3));
    Tolerance.CHOP.requireClose(x, HnProjection.INSTANCE.apply(x));
  }
}
