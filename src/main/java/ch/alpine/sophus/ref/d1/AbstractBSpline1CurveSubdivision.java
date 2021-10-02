// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.alpine.sophus.math.MidpointInterface;
import ch.alpine.tensor.ScalarQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;

/** linear B-spline
 * 
 * the scheme interpolates the control points
 * 
 * Dyn/Sharon 2014 p.14 show that the contractivity factor is mu = 1/2 */
public abstract class AbstractBSpline1CurveSubdivision implements CurveSubdivision, MidpointInterface {
  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    int length = tensor.length();
    if (1 < length)
      return stringNonEmpty(tensor).append(midpoint(Last.of(tensor), tensor.get(0)));
    ScalarQ.thenThrow(tensor);
    return tensor.copy();
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    int length = tensor.length();
    if (1 < length)
      return stringNonEmpty(tensor);
    ScalarQ.thenThrow(tensor);
    return tensor.copy();
  }

  private Tensor stringNonEmpty(Tensor tensor) {
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(2 * length);
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor p = iterator.next();
    list.add(p.copy());
    while (iterator.hasNext()) {
      list.add(midpoint(p, p = iterator.next()));
      list.add(p.copy());
    }
    return Unprotect.using(list);
  }
}
