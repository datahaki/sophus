// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class EmptyResultTest extends TestCase {
  public void testSimple() {
    assertEquals(EmptyResult.INSTANCE.errors(), Tensors.empty());
  }
}
