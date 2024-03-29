// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BinaryAverage;

/** dual scheme */
public class Split2LoDual3PointCurveSubdivision extends Dual3PointCurveSubdivision {
  /** @param binaryAverage non-null
   * @param p_qr
   * @param q_r
   * @return */
  public static CurveSubdivision of(BinaryAverage binaryAverage, Scalar p_qr, Scalar q_r) {
    return new Split2LoDual3PointCurveSubdivision(binaryAverage, p_qr, q_r);
  }

  // ---
  private final Scalar p_q;
  private final Scalar pq_r;
  private final Scalar q_r;
  private final Scalar p_qr;

  private Split2LoDual3PointCurveSubdivision(BinaryAverage binaryAverage, Scalar p_q, Scalar pq_r) {
    super(binaryAverage);
    this.p_q = p_q;
    this.pq_r = pq_r;
    q_r = RealScalar.ONE.subtract(p_q);
    p_qr = RealScalar.ONE.subtract(pq_r);
  }

  @Override // from Dual3PointCurveSubdivision
  public Tensor lo(Tensor p, Tensor q, Tensor r) {
    Tensor pq = binaryAverage.split(p, q, p_q);
    return binaryAverage.split(pq, r, pq_r);
  }

  @Override // from Dual3PointCurveSubdivision
  public Tensor hi(Tensor p, Tensor q, Tensor r) {
    Tensor qr = binaryAverage.split(q, r, q_r);
    return binaryAverage.split(p, qr, p_qr);
  }
}
