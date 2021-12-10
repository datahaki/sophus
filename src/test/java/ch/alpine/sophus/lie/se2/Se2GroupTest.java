// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class Se2GroupTest extends TestCase {
  public void testSimple() {
    Se2GroupElement se2GroupElement = Se2Group.INSTANCE.element(Tensors.vector(1, 2, 2 * Math.PI + 3));
    Tensor tensor = se2GroupElement.combine(Tensors.vector(0, 0, 0));
    assertEquals(tensor, Tensors.vector(1, 2, 3));
  }

  public void testUnits() {
    Tensor p = Tensors.fromString("{4.9[m], 4.9[m], 0.9}");
    Tensor q = Tensors.fromString("{5.0[m], 5.0[m], 1.0}");
    Tensor r = Tensors.fromString("{5.1[m], 5.1[m], 1.1}");
    Tensor s = Tensors.fromString("{4.8[m], 5.2[m], 1.3}");
    Se2GroupElement gp = Se2Group.INSTANCE.element(p);
    Se2GroupElement gq = Se2Group.INSTANCE.element(q);
    Se2GroupElement gr = Se2Group.INSTANCE.element(r);
    Se2GroupElement gs = Se2Group.INSTANCE.element(s);
    gp.combine(q);
    gp.combine(r);
    gp.combine(s);
    gq.combine(p);
    gq.combine(r);
    gq.combine(s);
    gr.combine(p);
    gr.combine(q);
    gr.combine(s);
    gs.combine(p);
    gs.combine(q);
    gs.combine(r);
  }
}
