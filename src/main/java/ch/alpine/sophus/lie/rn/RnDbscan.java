// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import ch.alpine.sophus.math.MinMax;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.EuclideanNdCenter;
import ch.alpine.tensor.opt.nd.NdMap;
import ch.alpine.tensor.opt.nd.NdMatch;
import ch.alpine.tensor.opt.nd.NdTreeMap;
import ch.alpine.tensor.opt.nd.SphericalNdCluster;

public class RnDbscan {
  private static final int NOISE = -1;
  // ---
  private final Scalar eps;
  private final int minPts;

  public RnDbscan(Scalar eps, int minPts) {
    this.eps = eps;
    this.minPts = minPts;
  }

  public void some(Tensor points) {
    MinMax minMax = MinMax.of(points);
    NdMap<Integer> ndMap = NdTreeMap.of(minMax.min(), minMax.max(), 5); // magic const
    {
      int index = -1;
      for (Tensor p : points)
        ndMap.add(p, ++index);
    }
    // ---
    Integer[] labels = new Integer[points.length()];
    int index = 0;
    int cluster = 0;
    for (Tensor p : points) {
      if (Objects.isNull(labels[index])) {
        Collection<NdMatch<Integer>> collection = SphericalNdCluster.of(ndMap, EuclideanNdCenter.of(p), eps);
        if (collection.size() < minPts) {
          labels[index] = NOISE; // noise
        } else {
          ++cluster;
          labels[index] = cluster;
          Set<Tensor> seeds = null;
          while (!seeds.isEmpty()) {
          }
        }
      }
      ++index;
    }
  }
}
