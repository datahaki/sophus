// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

/** subdivision scheme with linear subdivision for mid-point insertion and
 * LaneRiesenfeldCurveSubdivision with degree 3 for vertex reposition.
 * 
 * the computational complexity of LaneRiesenfeld3CurveSubdivision is
 * between cubic bspline and LaneRiesenfeldCurveSubdivision with degree 3.
 * 
 * Reference:
 * "A theoretical development for the computer generation of piecewise polynomial surfaces"
 * by J. M. Lane and R. F. Riesenfeld; IEEE Trans. Pattern Anal. Machine Intell. 2 (1980), 35-46 */
public final class LaneRiesenfeld3CurveSubdivision extends AbstractBSpline3CurveSubdivision implements Serializable {
  /** @param geodesicSpace
   * @return */
  public static CurveSubdivision of(GeodesicSpace geodesicSpace) {
    return new LaneRiesenfeld3CurveSubdivision(Objects.requireNonNull(geodesicSpace));
  }

  // ---
  private final GeodesicSpace geodesicSpace;

  private LaneRiesenfeld3CurveSubdivision(GeodesicSpace geodesicSpace) {
    this.geodesicSpace = geodesicSpace;
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    List<Tensor> list = new ArrayList<>(2 * length);
    Tensor pq = midpoint(Last.of(tensor), tensor.get(0));
    for (int index = 0; index < length; /* nothing */ ) {
      Tensor q = tensor.get(index);
      Tensor r = tensor.get(++index % length);
      Tensor qr = midpoint(q, r);
      list.add(center(pq, q, qr));
      list.add(qr);
      pq = qr;
    }
    Integers.requireEquals(list.size(), 2 * length);
    return Unprotect.using(list);
  }

  @Override // from AbstractBSpline3CurveSubdivision
  protected Tensor refine(Tensor tensor) {
    int length = tensor.length();
    int capacity = 2 * length - 1;
    List<Tensor> list = new ArrayList<>(capacity);
    Tensor pq;
    {
      Tensor q = tensor.get(0);
      Tensor r = tensor.get(1);
      pq = midpoint(q, r); // notation is deliberate
      list.add(q);
      list.add(pq);
    }
    int last = length - 1;
    for (int index = 1; index < last; /* nothing */ ) {
      Tensor q = tensor.get(index);
      Tensor r = tensor.get(++index);
      Tensor qr = midpoint(q, r);
      list.add(center(pq, q, qr));
      list.add(qr);
      pq = qr;
    }
    list.add(tensor.get(last));
    Integers.requireEquals(list.size(), capacity);
    return Unprotect.using(list);
  }

  @Override // from AbstractBSpline3CurveSubdivision
  protected Tensor center(Tensor pq, Tensor q, Tensor qr) {
    return midpoint(midpoint(pq, q), midpoint(q, qr));
  }

  @Override // from AbstractBSpline1CurveSubdivision
  public Tensor midpoint(Tensor p, Tensor q) {
    return geodesicSpace.midpoint(p, q);
  }
}
