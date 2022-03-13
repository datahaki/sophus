// code by jph
package ch.alpine.sophus.math;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class DirectedEdgeTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    IntDirectedEdge directedEdge = Serialization.copy(new IntDirectedEdge(2, 3));
    assertEquals(directedEdge.reverse(), new IntDirectedEdge(3, 2));
  }
}
