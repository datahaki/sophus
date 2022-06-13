// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Serialization;

class DirectedEdgeTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    IntDirectedEdge directedEdge = Serialization.copy(new IntDirectedEdge(2, 3));
    assertEquals(directedEdge.reverse(), new IntDirectedEdge(3, 2));
  }
}
