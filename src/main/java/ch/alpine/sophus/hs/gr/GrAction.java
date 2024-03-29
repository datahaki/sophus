// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.sca.Chop;

/** the pole ladder is exact in symmetric spaces */
public record GrAction(Tensor g) implements TensorUnaryOperator {
  /** @param g from SO(n) */
  public GrAction {
    OrthogonalMatrixQ.require(g);
  }

  @Override
  public Tensor apply(Tensor p) {
    return BasisTransform.ofMatrix(p, g); // g^-1 . p . g
  }

  /** @param p
   * @param q
   * @return group element that maps p to q via {@link BasisTransform#ofMatrix(Tensor, Tensor)} */
  public static Tensor match(Tensor p, Tensor q) {
    Eigensystem ep = Eigensystem.ofSymmetric(p);
    Eigensystem eq = Eigensystem.ofSymmetric(q);
    // eigenvalue comparison to verify that p and q are from the same space Gr(n, k)
    Chop._08.requireClose(ep.values(), eq.values());
    return Transpose.of(ep.vectors()).dot(eq.vectors());
  }
}
