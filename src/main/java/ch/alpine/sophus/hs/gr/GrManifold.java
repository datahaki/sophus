// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.gr.InfluenceMatrixQ;
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
public class GrManifold implements HomogeneousSpace, MetricManifold, Serializable {
  public static final InfluenceMatrixQ INFLUENCE_MATRIX_Q = new InfluenceMatrixQ(Chop._08); // 1e-10 does not always work
  private static final Scalar N1_4 = RationalScalar.of(-1, 4);
  public static final GrManifold INSTANCE = new GrManifold();
  // ---

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._10);
  }

  @Override // from Manifold
  public GrExponential exponential(Tensor x) {
    return new GrExponential(x);
  }

  @Override
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  /** rank can be determined via {@link Eigensystem} */
  @Override // from MemberQ
  public boolean isMember(Tensor p) {
    return INFLUENCE_MATRIX_Q.isMember(p);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    // matrix bracket is obsolete
    return BasisTransform.ofMatrix(p, MatrixExp.of(exponential(p).mLog(q).multiply(RationalScalar.HALF)));
  }

  @Override
  public Tensor midpoint(Tensor p, Tensor q) {
    return BasisTransform.ofMatrix(p, MatrixExp.of(exponential(p).mLog(q).multiply(N1_4)));
  }

  @Override
  public String toString() {
    return "Gr";
  }
}
