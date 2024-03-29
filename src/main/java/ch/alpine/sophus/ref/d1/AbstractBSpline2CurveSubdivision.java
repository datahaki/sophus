// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.Iterator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.chq.ScalarQ;

/** base class for B-Spline degree 2 curve subdivision
 * Chaikin 1965 */
public abstract class AbstractBSpline2CurveSubdivision implements CurveSubdivision {
  @Override // from CurveSubdivision
  public final Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    return refine(protected_string(tensor), Last.of(tensor), tensor.get(0));
  }

  // Hint: curve contracts at the sides
  @Override // from CurveSubdivision
  public final Tensor string(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    return protected_string(tensor);
  }

  private Tensor protected_string(Tensor tensor) {
    int length = tensor.length();
    Tensor curve = Tensors.reserve(2 * length); // allocation for cyclic case
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor p = iterator.next();
    while (iterator.hasNext())
      refine(curve, p, p = iterator.next());
    return curve;
  }

  /** @param curve
   * @param p
   * @param q
   * @return curve with points [p, q]1/4 and [p, q]3/4 appended */
  protected abstract Tensor refine(Tensor curve, Tensor p, Tensor q);
}
