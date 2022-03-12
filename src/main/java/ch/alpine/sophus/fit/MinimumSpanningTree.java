// code by jph
package ch.alpine.sophus.fit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.alpine.sophus.math.UndirectedEdge;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

/** <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MinimumSpanningTree.html">MinimumSpanningTree</a> */
public enum MinimumSpanningTree {
  ;
  public static record EdgeComparator(Tensor matrix) implements Comparator<UndirectedEdge> {
    @Override
    public int compare(UndirectedEdge edge1, UndirectedEdge edge2) {
      return Scalars.compare( //
          edge1.Get(matrix), //
          edge2.Get(matrix));
    }
  }

  /** uses Prim's algorithm to find minimum spanning tree
   * 
   * @param matrix symmetric
   * @return list of edges unordered
   * @throws Exception if given matrix is not symmetric */
  public static List<UndirectedEdge> of(Tensor matrix) {
    SymmetricMatrixQ.require(matrix);
    int n = matrix.length();
    if (n == 0)
      return new ArrayList<>();
    List<UndirectedEdge> list = new ArrayList<>(n - 1);
    Set<Integer> visited = new HashSet<>();
    visited.add(0);
    Set<Integer> unknown = IntStream.range(1, n).boxed().collect(Collectors.toSet());
    while (!unknown.isEmpty()) {
      Scalar min = null;
      UndirectedEdge directedEdge = null;
      int jbest = -1;
      for (int i : visited)
        for (int j : unknown) {
          Scalar cmp = matrix.Get(i, j);
          if (Objects.isNull(min) || Scalars.lessThan(cmp, min)) {
            min = cmp;
            directedEdge = new UndirectedEdge(i, j);
            jbest = j;
          }
        }
      int index = jbest;
      visited.add(index);
      unknown.remove(index);
      list.add(directedEdge);
    }
    return list;
  }
}
