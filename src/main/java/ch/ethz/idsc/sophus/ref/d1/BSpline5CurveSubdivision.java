// code by jph
package ch.ethz.idsc.sophus.ref.d1;

import ch.ethz.idsc.sophus.math.Nocopy;
import ch.ethz.idsc.sophus.math.SplitInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.ScalarQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Last;

/** quintic B-spline is implemented as an extension of
 * cubic B-spline refinement */
public class BSpline5CurveSubdivision extends BSpline3CurveSubdivision {
  private static final Scalar _5_8 = RationalScalar.of(5, 8);
  private static final Scalar _15_16 = RationalScalar.of(15, 16);
  private static final Scalar _3_8 = RationalScalar.of(3, 8);
  private static final Scalar _1_16 = RationalScalar.of(1, 16);

  /** @param splitInterface */
  public BSpline5CurveSubdivision(SplitInterface splitInterface) {
    super(splitInterface);
  }

  @Override // from AbstractBSpline3CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    if (length < 2)
      return tensor.copy();
    Nocopy curve = new Nocopy(2 * length);
    Tensor p = Last.of(tensor);
    Tensor q = tensor.get(0);
    Tensor r = tensor.get(1);
    for (int index = 0; index < length; ++index)
      curve //
          .append(quinte(p, q, r)) //
          .append(center(p, p = q, q = r, r = tensor.get((index + 2) % length)));
    return curve.tensor();
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    if (tensor.length() < 4)
      return super.string(tensor); // cubic BSpline3
    return private_refine(tensor);
  }

  private Tensor private_refine(Tensor tensor) {
    int length = tensor.length();
    Nocopy curve = new Nocopy(2 * length);
    {
      Tensor q = tensor.get(0);
      Tensor r = tensor.get(1);
      curve.append(q);
      curve.append(midpoint(q, r));
    }
    {
      Tensor p = tensor.get(0);
      Tensor q = tensor.get(1);
      Tensor r = tensor.get(2);
      for (int index = 3; index < length; ++index)
        curve //
            .append(quinte(p, q, r)) //
            .append(center(p, p = q, q = r, r = tensor.get(index)));
    }
    {
      int last = length - 1;
      Tensor s = tensor.get(last - 2);
      Tensor r = tensor.get(last - 1);
      Tensor q = tensor.get(last);
      curve.append(quinte(s, r, q));
      curve.append(midpoint(r, q));
      curve.append(q);
    }
    return curve.tensor();
  }

  // reposition of point q
  private Tensor quinte(Tensor p, Tensor q, Tensor r) {
    return midpoint( //
        splitInterface.split(p, q, _5_8), //
        splitInterface.split(q, r, _3_8));
  }

  // insertion between points q and r
  private Tensor center(Tensor p, Tensor q, Tensor r, Tensor s) {
    return midpoint( //
        splitInterface.split(p, q, _15_16), //
        splitInterface.split(r, s, _1_16));
  }
}
