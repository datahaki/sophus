// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

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
 * Gr(n, k) is a homogoneous space, quotient of the special orthogonal
 * group by the subgroup of rotations stabilising a k-dimensional subspace:
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
 * Gr(n, 1) ~ S^(n - 1) / {+-1} */
public enum GrManifold implements HsExponential {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor x) {
    return new GrExponential(x);
  }

  @Override // from HsExponential
  public Exponential exponential(Tensor x) {
    return new GrExponential(x);
  }
}
