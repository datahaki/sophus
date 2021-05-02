// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class EmptyResultTest extends TestCase {
  public void testSimple() {
    assertEquals(EmptyResult.INSTANCE.errors(), Tensors.empty());
  }
}
