// code by jph
package ch.alpine.sophus.math;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class UndirectedEdgeTest extends TestCase {
  public void testSimple() {
    IntUndirectedEdge undirectedEdge = new IntUndirectedEdge(2, 3);
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
    assertEquals(undirectedEdge, new IntUndirectedEdge(3, 2));
  }

  public void testCorrect() throws ClassNotFoundException, IOException {
    IntUndirectedEdge undirectedEdge = Serialization.copy(new IntUndirectedEdge(3, 2));
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
  }
}
