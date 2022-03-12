// code by jph
package ch.alpine.sophus.math;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class DirectedEdgeTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    DirectedEdge directedEdge = Serialization.copy(new DirectedEdge(2, 3));
    assertEquals(directedEdge.reverse(), new DirectedEdge(3, 2));
  }
}
