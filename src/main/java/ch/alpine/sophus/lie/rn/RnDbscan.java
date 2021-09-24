// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.alpine.sophus.math.MinMax;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.opt.nd.NdCenterInterface;
import ch.alpine.tensor.opt.nd.NdClusterRadius;
import ch.alpine.tensor.opt.nd.NdMap;
import ch.alpine.tensor.opt.nd.NdMatch;
import ch.alpine.tensor.opt.nd.NdTreeMap;
import ch.alpine.tensor.sca.Sign;

/** Density-based spatial clustering of applications with noise
 * 
 * Reference: Wikipedia */
public class RnDbscan {
  private static final int NOISE = -1;
  // ---
  private final Scalar eps;
  private final int minPts;

  public RnDbscan(Scalar eps, int minPts) {
    this.eps = Sign.requirePositiveOrZero(eps);
    this.minPts = Integers.requirePositive(minPts);
  }

  public Integer[] cluster(Tensor points, Function<Tensor, NdCenterInterface> function) {
    NdMap<Integer> ndMap = NdTreeMap.of(MinMax.ndBox(points));
    {
      int index = -1;
      for (Tensor point : points)
        ndMap.add(point, ++index);
    }
    // ---
    Integer[] labels = new Integer[points.length()];
    int index = 0;
    int cluster = -1;
    for (Tensor p : points) {
      if (Objects.isNull(labels[index])) { // else "Previously processed in inner loop"
        Collection<NdMatch<Integer>> c_p = //
            NdClusterRadius.of(ndMap, function.apply(p), eps); // "Find neighbors"
        if (c_p.size() < minPts) { // "Density check"
          labels[index] = NOISE; // "Label as Noise"
        } else {
          ++cluster; // "next cluster label"
          labels[index] = cluster; // "Label initial point"
          // ---
          Set<Integer> set = c_p.stream().map(NdMatch::value).collect(Collectors.toSet());
          boolean removed = set.remove(index);
          if (!removed)
            System.err.println("not removed?");
          while (!set.isEmpty()) {
            int index_q = set.iterator().next(); // "Process every seed point Q"
            set.remove(index_q);
            Integer label_q = labels[index_q];
            if (Objects.nonNull(label_q) && label_q.equals(NOISE))
              labels[index_q] = cluster; // "Change Noise to border point"
            if (Objects.isNull(labels[index_q])) { // else "Previously processed (e.g., border point)"
              labels[index_q] = cluster; // "Label neighbor"
              Tensor q = points.get(index_q);
              Collection<NdMatch<Integer>> c_q = //
                  NdClusterRadius.of(ndMap, function.apply(q), eps); // "Find neighbors"
              if (minPts <= c_q.size()) // "Density check (if Q is a core point)"
                c_p.stream().map(NdMatch::value).forEach(set::add); // "Add new neighbors to seed set"
            }
          }
        }
      }
      ++index;
    }
    return labels;
  }
}
