// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.BiinvariantMeans;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** cubic B-spline
 * 
 * uses biinvariant mean */
public final class MSpline3CurveSubdivision extends RefiningBSpline3CurveSubdivision implements Serializable {
  private static final Tensor MASK_MIDDLE = Tensors.vector(4, 4).divide(RealScalar.of(8));
  private static final Tensor MASK_CENTER = Tensors.vector(1, 6, 1).divide(RealScalar.of(8));
  // ---
  private final TensorUnaryOperator middle;
  private final TensorUnaryOperator center;

  /** @param biinvariantMean */
  public MSpline3CurveSubdivision(BiinvariantMean biinvariantMean) {
    middle = BiinvariantMeans.of(biinvariantMean, MASK_MIDDLE);
    center = BiinvariantMeans.of(biinvariantMean, MASK_CENTER);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor q, Tensor r) {
    return middle.apply(Unprotect.byRef(q, r));
  }

  @Override // from AbstractBSpline3CurveSubdivision
  protected Tensor center(Tensor p, Tensor q, Tensor r) {
    return center.apply(Unprotect.byRef(p, q, r));
  }
}
