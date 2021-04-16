// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BasisTransform;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.ev.Eigensystem;
import ch.ethz.idsc.tensor.sca.Chop;

/** the pole ladder is exact in symmetric spaces */
public class GrAction implements TensorUnaryOperator {
  private final Tensor g;

  /** @param g from SO(n) */
  public GrAction(Tensor g) {
    this.g = OrthogonalMatrixQ.require(g);
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
    Chop._08.requireClose(ep.values(), eq.values());
    return Transpose.of(ep.vectors()).dot(eq.vectors());
  }
}
