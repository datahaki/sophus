// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.Iterator;

import ch.alpine.sophus.math.MidpointInterface;
import ch.alpine.sophus.math.Nocopy;
import ch.alpine.tensor.ScalarQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.ext.Integers;

/** Reference:
 * "A theoretical development for the computer generation of piecewise polynomial surfaces"
 * by J. M. Lane and R. F. Riesenfeld; IEEE Trans. Pattern Anal. Machine Intell. 2 (1980), 35-46 */
public class LaneRiesenfeldCurveSubdivision implements CurveSubdivision, Serializable {
  /** @param midpointInterface
   * @param degree strictly positive
   * @return
   * @throws Exception if midpointInterface is null */
  public static CurveSubdivision of(MidpointInterface midpointInterface, int degree) {
    return new LaneRiesenfeldCurveSubdivision(midpointInterface, Integers.requirePositive(degree));
  }

  /***************************************************/
  /** linear curve subdivision */
  private final BSpline1CurveSubdivision bSpline1CurveSubdivision;
  private final int degree;

  private LaneRiesenfeldCurveSubdivision(MidpointInterface midpointInterface, int degree) {
    bSpline1CurveSubdivision = new BSpline1CurveSubdivision(midpointInterface);
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
      Nocopy nocopy = new Nocopy(value.length());
      if (Integers.isEven(count)) {
        Tensor p = value.get(0);
        for (int index = 1; index < value.length(); ++index)
          nocopy.append(bSpline1CurveSubdivision.midpoint(p, p = value.get(index)));
        nocopy.append(bSpline1CurveSubdivision.midpoint(p, p = value.get(0)));
      } else {
        Tensor p = Last.of(value);
        for (int index = 0; index < value.length(); ++index)
          nocopy.append(bSpline1CurveSubdivision.midpoint(p, p = value.get(index)));
      }
      tensor = value;
      value = nocopy.tensor();
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
      Nocopy nocopy = new Nocopy(value.length() + 1);
      if (odd)
        nocopy.append(tensor.get(0));
      Iterator<Tensor> iterator = value.iterator();
      Tensor p = iterator.next();
      while (iterator.hasNext())
        nocopy.append(bSpline1CurveSubdivision.midpoint(p, p = iterator.next()));
      if (odd)
        nocopy.append(Last.of(tensor));
      tensor = value;
      value = nocopy.tensor();
    }
    return value;
  }
}
