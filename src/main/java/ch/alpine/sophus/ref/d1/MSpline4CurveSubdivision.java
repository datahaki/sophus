// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.BiinvariantMeans;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.BinaryAverage;

public class MSpline4CurveSubdivision extends Dual3PointCurveSubdivision {
  private static final Tensor MASK_LO = Tensors.vector(5, 10, 1).divide(RealScalar.of(16));
  private static final Tensor MASK_HI = Tensors.vector(1, 10, 5).divide(RealScalar.of(16));

  /** @param biinvariantMean
   * @return */
  public static CurveSubdivision of(BiinvariantMean biinvariantMean) {
    BinaryAverage binaryAverage = new BinaryAverage() {
      @Override
      public Tensor split(Tensor p, Tensor q, Scalar scalar) {
        return biinvariantMean.mean(Unprotect.byRef(p, q), Tensors.of(RealScalar.ONE.subtract(scalar), scalar));
      }
    };
    return new MSpline4CurveSubdivision(binaryAverage, biinvariantMean);
  }

  // ---
  private final TensorUnaryOperator lo;
  private final TensorUnaryOperator hi;

  private MSpline4CurveSubdivision(BinaryAverage binaryAverage, BiinvariantMean biinvariantMean) {
    super(binaryAverage);
    lo = BiinvariantMeans.of(biinvariantMean, MASK_LO);
    hi = BiinvariantMeans.of(biinvariantMean, MASK_HI);
  }

  @Override
  public Tensor lo(Tensor p, Tensor q, Tensor r) {
    return lo.apply(Unprotect.byRef(p, q, r));
  }

  @Override
  public Tensor hi(Tensor p, Tensor q, Tensor r) {
    return hi.apply(Unprotect.byRef(p, q, r));
  }
}
