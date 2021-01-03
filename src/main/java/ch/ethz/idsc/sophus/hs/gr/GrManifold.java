package ch.ethz.idsc.sophus.hs.gr;

/** Quote from geomstats:
 * "Manifold of linear subspaces.
 * 
 * The Grassmannian Gr(n, k) is the manifold of k-dimensional
 * subspaces in n-dimensional Euclidean space.
 * 
 * Gr(n, k) is represented by n x matrices of rank k satisfying P.P = P and P' = P
 * 
 * Gr(n, k) is a homogoneous space, quotient of the special orthogonal
 * group by the subgroup of rotations stabilising a k-dimensional subspace:
 * 
 * Gr(n, k) ~ SO(n)/(SO(k) x SO(n-k))
 * 
 * It is therefore customary to represent the Grassmannian by equivalence classes of
 * orthogonal k-frames in R^n.
 * 
 * For such a representation, work in the Stiefel manifold instead:
 * Gr(n, k) ~ St(n, k) / SO(k) */
public enum GrManifold {
  // ---
}
