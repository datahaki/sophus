// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

/** dual scheme */
public class Dual4PointCurveSubdivision implements CurveSubdivision, Serializable {
  private final GeodesicSpace geodesicSpace;
  private final Scalar lo_pq;
  private final Scalar lo_rs;
  private final Scalar lo_pqrs;
  private final Scalar hi_pq;
  private final Scalar hi_rs;
  private final Scalar hi_pqrs;

  /** @param geodesicSpace non-null
   * @param pq_f
   * @param rs_f
   * @param pqrs */
  public Dual4PointCurveSubdivision( //
      GeodesicSpace geodesicSpace, //
      Scalar pq_f, Scalar rs_f, Scalar pqrs) {
    this.geodesicSpace = Objects.requireNonNull(geodesicSpace);
    this.lo_pq = pq_f;
    this.lo_rs = rs_f;
    this.lo_pqrs = pqrs;
    hi_pq = RealScalar.ONE.subtract(lo_rs);
    hi_rs = RealScalar.ONE.subtract(lo_pq);
    hi_pqrs = RealScalar.ONE.subtract(lo_pqrs);
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    List<Tensor> list = new ArrayList<>(2 * length);
    Tensor p = Last.of(tensor);
    Tensor q = tensor.get(0);
    Tensor r = tensor.get(1);
    for (int index = 0; index < length; ++index) {
      Tensor s = tensor.get((index + 2) % length);
      ScalarTensorFunction c_pq = geodesicSpace.curve(p, q);
      ScalarTensorFunction c_rs = geodesicSpace.curve(r, s);
      list.add(lo(c_pq, c_rs));
      list.add(hi(c_pq, c_rs));
      p = q;
      q = r;
      r = s;
    }
    Integers.requireEquals(list.size(), 2 * length);
    return Unprotect.using(list);
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  // @return point between q and r but more towards q
  private Tensor lo(ScalarTensorFunction c_pq, ScalarTensorFunction c_rs) {
    Tensor pq = c_pq.apply(lo_pq);
    Tensor rs = c_rs.apply(lo_rs);
    return geodesicSpace.split(pq, rs, lo_pqrs);
  }

  // @return point between q and r but more towards r
  private Tensor hi(ScalarTensorFunction c_pq, ScalarTensorFunction c_rs) {
    Tensor pq = c_pq.apply(hi_pq);
    Tensor rs = c_rs.apply(hi_rs);
    return geodesicSpace.split(pq, rs, hi_pqrs);
  }
}
