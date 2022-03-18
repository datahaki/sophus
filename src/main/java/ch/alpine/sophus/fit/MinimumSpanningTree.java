// code by jph
package ch.alpine.sophus.fit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.alpine.sophus.math.IntUndirectedEdge;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

public enum MinimumSpanningTree {
  ;
  /** uses Prim's algorithm to find minimum spanning tree
   * 
   * @param matrix symmetric
   * @return list of undirected edges unordered
   * @throws Exception if given matrix is not symmetric */
  public static List<IntUndirectedEdge> of(Tensor matrix) {
    SymmetricMatrixQ.require(matrix);
    int n = matrix.length();
    if (n == 0)
      return new ArrayList<>();
    List<IntUndirectedEdge> list = new ArrayList<>(n - 1);
    Set<Integer> visited = new HashSet<>();
    visited.add(0);
    Set<Integer> unknown = IntStream.range(1, n).boxed().collect(Collectors.toSet());
    while (!unknown.isEmpty()) {
      Scalar min = null;
      IntUndirectedEdge undirectedEdge = null;
      int index = -1;
      for (int i : visited)
        for (int j : unknown) {
          Scalar cmp = matrix.Get(i, j);
          if (Objects.isNull(min) || Scalars.lessThan(cmp, min)) {
            min = cmp;
            undirectedEdge = new IntUndirectedEdge(i, j);
            index = j;
          }
        }
      visited.add(index);
      unknown.remove(index);
      list.add(undirectedEdge);
    }
    return list;
  }
}
