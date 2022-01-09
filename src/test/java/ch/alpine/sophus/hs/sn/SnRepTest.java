// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import junit.framework.TestCase;

public class SnRepTest extends TestCase {
  public void testSimple() {
    SnRep snRep = new SnRep(2);
    Tensor p = Vector2Norm.NORMALIZE.apply(Tensors.vector(0.10, +0.2, 1));
    Tensor matrix = snRep.toGroupElementMatrix(p);
    Tensor point = snRep.toPoint(matrix);
    Tolerance.CHOP.requireClose(p, point);
  }
}
