// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** Quote from geomstats:
 * "Manifold of linear subspaces.
 * 
 * The Grassmann manifold Gr(n, k) is the manifold of k-dimensional
 * subspaces in n-dimensional Euclidean space.
 * 
 * Gr(n, k) is represented by n x matrices of rank k satisfying P.P = P and P' = P
 * 
 * The dimensionality of Gr(n, k) is k * (n - k).
 * 
 * Gr(n, k) is a homogeneous space, quotient of the special orthogonal
 * group by the subgroup of rotations stabilizing a k-dimensional subspace:
 * 
 * Gr(n, k) ~ O(n) / (O(k) x O(n-k))
 * Gr(n, k) ~ SO(n) / (SO(k) x SO(n-k))
 * 
 * It is therefore customary to represent the Grassmann manifold by equivalence classes
 * of orthogonal k-frames in R^n.
 * 
 * For such a representation, work in the Stiefel manifold instead:
 * Gr(n, k) ~ St(n, k) / SO(k)
 * 
 * Gr(n, 1) ~ S^(n - 1) / {+-1}
 * 
 * The first Grassmann manifold that is not isomorphic to a projective space is Gr(4, 2)
 * 
 * distance(p, q) == FrobeniusNorm[new GrExponential(p).log(q)]
 * 
 * Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * E. Batzies, K. Hueper, L. Machado, F. Silva Leite by 2015 */
public enum GrManifold implements HomogeneousSpace, MetricManifold {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor x) {
    return new GrExponential(x);
  }

  @Override
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.reduce(this, chop);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return norm(new GrExponential(p).vectorLog(q));
  }

  @Override
  public Scalar norm(Tensor v) {
    return LowerVectorize0_2Norm.INSTANCE.norm(v);
  }
}
