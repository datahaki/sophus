// code by jph
// documentation by claude
package ch.alpine.sophus.hs.gr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.sca.Chop;

/** action of the general linear group GL(n) on the Grassmannian Gr(n, k) */
public record GrAction(Tensor g) implements TensorUnaryOperator {
  /** Finds the GL(n) element mapping one Grassmannian point to another.
   * 
   * <p>Both p and q are assumed to be k-dimensional subspaces in ℝ^n,
   * represented as projection matrices.
   * 
   * @param p first Grassmannian point (projection matrix)
   * @param q second Grassmannian point (projection matrix)
   * @return group element g such that g·p = q
   * @throws Exception if p and q don't have matching eigenvalues */
  public static GrAction match(Tensor p, Tensor q) {
    Eigensystem ep = Eigensystem.ofSymmetric(p).decreasing();
    Eigensystem eq = Eigensystem.ofSymmetric(q).decreasing();
    // eigenvalue comparison to verify that p and q are from the same space Gr(n, k)
    Chop._10.requireClose(ep.values(), eq.values());
    return new GrAction(Transpose.of(ep.vectors()).dot(eq.vectors()));
  }

  /** @param g from SO(n) */
  public GrAction {
    OrthogonalMatrixQ.INSTANCE.require(g);
  }

  @Override
  public Tensor apply(Tensor p) {
    // return Dot.of(g, p, Transpose.of(g));
    return BasisTransform.ofMatrix(p, g); // g^-1 . p . g
  }
}
