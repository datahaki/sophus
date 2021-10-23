// code by jph
package ch.alpine.sophus.crv.dubins;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class RelaxDubinsTest extends TestCase {
  public void testSimple() {
    for (DubinsType dubinsType : DubinsType.values()) {
      Scalar maxRadius = DubinsRadius.getMax(Tensors.vector(1, 2, -1), dubinsType, Clips.interval(0.3, 1.3));
      System.out.println(maxRadius);
    }
  }
}
