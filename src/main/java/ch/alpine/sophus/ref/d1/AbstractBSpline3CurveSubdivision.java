// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.math.Nocopy;
import ch.alpine.tensor.ScalarQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Last;

/** examples of extensions are
 * LaneRiesenfeld3CurveSubdivision
 * BSpline5CurveSubdivision */
public abstract class AbstractBSpline3CurveSubdivision extends AbstractBSpline1CurveSubdivision {
  @Override // from AbstractBSpline1CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    Nocopy curve = new Nocopy(2 * length);
    Tensor p = Last.of(tensor);
    Tensor q = tensor.get(0);
    for (int index = 1; index <= length; ++index)
      curve.append(center(p, p = q, q = tensor.get(index % length))).append(midpoint(p, q));
    return curve.tensor();
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    switch (tensor.length()) {
    case 0:
      return Tensors.empty();
    case 1:
      return tensor.copy();
    default:
      return refine(tensor);
    }
  }

  /** @param tensor with at least 2 control points
   * @return subdivision of control points along string */
  protected abstract Tensor refine(Tensor tensor);

  /** @param p
   * @param q
   * @param r
   * @return replacement for control point q */
  protected abstract Tensor center(Tensor p, Tensor q, Tensor r);
}
