// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MinimumSpanningTree.html">MinimumSpanningTree</a> */
public enum MinimumSpanningTree {
  ;
  public static class Edge implements Serializable {
    public final int i;
    public final int j;

    public Edge(int i, int j) {
      this.i = i;
      this.j = j;
    }

    public Scalar Get(Tensor tensor) {
      return tensor.Get(i, j);
    }

    @Override
    public String toString() {
      return String.format("Edge[%d, %d]", i, j);
    }
  }

  public static class EdgeComparator implements Comparator<Edge> {
    private final Tensor matrix;

    public EdgeComparator(Tensor matrix) {
      this.matrix = matrix;
    }

    @Override
    public int compare(Edge edge1, Edge edge2) {
      return Scalars.compare(edge1.Get(matrix), edge2.Get(matrix));
    }
  }

  /** uses Prim's algorithm to find minimum spanning tree
   * 
   * @param matrix symmetric
   * @return list of edges unordered
   * @throws Exception if given matrix is not symmetric */
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
      Scalar min = null;
      Edge edge = null;
      for (int i : visited)
        for (int j : unknown) {
          Scalar cmp = matrix.Get(i, j);
          if (Objects.isNull(min) || Scalars.lessThan(cmp, min)) {
            min = cmp;
            edge = new Edge(i, j);
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
