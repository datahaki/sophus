// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

/** Reference:
 * "A theoretical development for the computer generation of piecewise polynomial surfaces"
 * by J. M. Lane and R. F. Riesenfeld; IEEE Trans. Pattern Anal. Machine Intell. 2 (1980), 35-46 */
public class LaneRiesenfeldCurveSubdivision implements CurveSubdivision, Serializable {
  /** @param geodesicSpace
   * @param degree strictly positive
   * @return
   * @throws Exception if geodesicSpace is null */
  public static CurveSubdivision of(GeodesicSpace geodesicSpace, int degree) {
    return new LaneRiesenfeldCurveSubdivision(geodesicSpace, Integers.requirePositive(degree));
  }

  // ---
  /** linear curve subdivision */
  private final BSpline1CurveSubdivision bSpline1CurveSubdivision;
  private final int degree;

  private LaneRiesenfeldCurveSubdivision(GeodesicSpace geodesicSpace, int degree) {
    bSpline1CurveSubdivision = new BSpline1CurveSubdivision(geodesicSpace);
    this.degree = degree;
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    Tensor value = bSpline1CurveSubdivision.cyclic(tensor);
    for (int count = 2; count <= degree; ++count) {
      List<Tensor> list = new ArrayList<>(value.length());
      if (Integers.isEven(count)) {
        Tensor p = value.get(0);
        for (int index = 1; index < value.length(); ++index)
          list.add(bSpline1CurveSubdivision.midpoint(p, p = value.get(index)));
        list.add(bSpline1CurveSubdivision.midpoint(p, p = value.get(0)));
      } else {
        Tensor p = Last.of(value);
        for (int index = 0; index < value.length(); ++index)
          list.add(bSpline1CurveSubdivision.midpoint(p, p = value.get(index)));
      }
      tensor = value;
      Integers.requireEquals(list.size(), value.length());
      value = Unprotect.using(list);
    }
    return value;
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    Tensor value = bSpline1CurveSubdivision.string(tensor);
    for (int count = 2; count <= degree; ++count) {
      boolean odd = !Integers.isEven(count);
      int capacity = value.length() + (odd ? 1 : -1);
      List<Tensor> list = new ArrayList<>(capacity);
      if (odd)
        list.add(tensor.get(0));
      Iterator<Tensor> iterator = value.iterator();
      Tensor p = iterator.next();
      while (iterator.hasNext())
        list.add(bSpline1CurveSubdivision.midpoint(p, p = iterator.next()));
      if (odd)
        list.add(Last.of(tensor));
      tensor = value;
      Integers.requireEquals(list.size(), capacity);
      value = Unprotect.using(list);
    }
    return value;
  }
}
