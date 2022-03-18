// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

public class UndirectedEdgeTest {
  @Test
  public void testSimple() {
    IntUndirectedEdge undirectedEdge = new IntUndirectedEdge(2, 3);
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
    assertEquals(undirectedEdge, new IntUndirectedEdge(3, 2));
  }

  @Test
  public void testCorrect() throws ClassNotFoundException, IOException {
    IntUndirectedEdge undirectedEdge = Serialization.copy(new IntUndirectedEdge(3, 2));
    assertEquals(undirectedEdge.i(), 2);
    assertEquals(undirectedEdge.j(), 3);
  }
}
