// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;

/** MinimumSpanningTree */
public class PrimAlgorithm {
  public static class Edge {
    public final int i;
    public final int j;

    public Edge(int i, int j) {
      this.i = i;
      this.j = j;
    }

    @Override
    public String toString() {
      return String.format("(%d, %d)", i, j);
    }
  }

  /** @param matrix
   * @return */
  public static List<Edge> of(Tensor matrix) {
    SymmetricMatrixQ.require(matrix);
    int n = matrix.length();
    if (n == 0)
      return new ArrayList<>();
    List<Edge> list = new ArrayList<>(n - 1);
    Set<Integer> visited = new HashSet<>();
    visited.add(0);
    Set<Integer> unknown = IntStream.range(1, n).boxed().collect(Collectors.toSet());
    while (!unknown.isEmpty()) {
      Scalar min = DoubleScalar.POSITIVE_INFINITY;
      Edge edge = null;
      for (int i : visited) {
        for (int j : unknown) {
          Scalar cmp = matrix.Get(i, j);
          if (Scalars.lessThan(cmp, min)) {
            min = cmp;
            edge = new Edge(i, j);
          }
        }
      }
      int index = edge.j;
      visited.add(index);
      unknown.remove(index);
      list.add(edge);
    }
    return list;
  }
}
