// code by jph
package ch.alpine.sophus.math;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class UndirectedEdgeTest extends TestCase {
  public void testSimple() {
    UndirectedEdge undirectedEdge = new UndirectedEdge(2, 3);
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
    assertEquals(undirectedEdge, new UndirectedEdge(3, 2));
  }

  public void testCorrect() throws ClassNotFoundException, IOException {
    UndirectedEdge undirectedEdge = Serialization.copy(new UndirectedEdge(3, 2));
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
  }
}
