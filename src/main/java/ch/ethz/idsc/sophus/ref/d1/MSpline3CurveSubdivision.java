// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMeans;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** cubic B-spline
 * 
 * uses biinvariant mean */
public final class MSpline3CurveSubdivision extends RefiningBSpline3CurveSubdivision implements Serializable {
  private static final Tensor MASK_MID = Tensors.vector(4, 4).divide(RealScalar.of(8));
  private static final Tensor MASK_CEN = Tensors.vector(1, 6, 1).divide(RealScalar.of(8));
  // ---
  private final TensorUnaryOperator midpoint;
  private final TensorUnaryOperator center;

  public MSpline3CurveSubdivision(BiinvariantMean biinvariantMean) {
    midpoint = BiinvariantMeans.of(biinvariantMean, MASK_MID);
    center = BiinvariantMeans.of(biinvariantMean, MASK_CEN);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor q, Tensor r) {
    return midpoint.apply(Unprotect.byRef(q, r));
  }

  @Override // from AbstractBSpline3CurveSubdivision
  protected Tensor center(Tensor p, Tensor q, Tensor r) {
    return center.apply(Unprotect.byRef(p, q, r));
  }
}