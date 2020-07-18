// code by jph
package ch.ethz.idsc.sophus.crv.subdiv;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.BiinvariantMeans;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.opt.BinaryAverage;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

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

  /***************************************************/
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
