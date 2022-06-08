// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

/** quintic B-spline is implemented as an extension of
 * cubic B-spline refinement */
public class BSpline5CurveSubdivision extends BSpline3CurveSubdivision {
  private static final Scalar _5_8 = RationalScalar.of(5, 8);
  private static final Scalar _15_16 = RationalScalar.of(15, 16);
  private static final Scalar _3_8 = RationalScalar.of(3, 8);
  private static final Scalar _1_16 = RationalScalar.of(1, 16);

  /** @param geodesicSpace */
  public BSpline5CurveSubdivision(GeodesicSpace geodesicSpace) {
    super(geodesicSpace);
  }

  @Override // from AbstractBSpline3CurveSubdivision
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
      list.add(quinte(p, q, r));
      list.add(center(p, p = q, q = r, r = tensor.get((index + 2) % length)));
    }
    Integers.requireEquals(list.size(), 2 * length);
    return Unprotect.using(list);
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    if (tensor.length() < 4)
      return super.string(tensor); // cubic BSpline3
    return private_refine(tensor);
  }

  private Tensor private_refine(Tensor tensor) {
    int length = tensor.length();
    int capacity = 2 * length - 1;
    List<Tensor> list = new ArrayList<>(capacity);
    {
      Tensor q = tensor.get(0);
      Tensor r = tensor.get(1);
      list.add(q);
      list.add(midpoint(q, r));
    }
    {
      Tensor p = tensor.get(0);
      Tensor q = tensor.get(1);
      Tensor r = tensor.get(2);
      for (int index = 3; index < length; ++index) {
        list.add(quinte(p, q, r));
        list.add(center(p, p = q, q = r, r = tensor.get(index)));
      }
    }
    {
      int last = length - 1;
      Tensor s = tensor.get(last - 2);
      Tensor r = tensor.get(last - 1);
      Tensor q = tensor.get(last);
      list.add(quinte(s, r, q));
      list.add(midpoint(r, q));
      list.add(q);
    }
    Integers.requireEquals(list.size(), capacity);
    return Unprotect.using(list);
  }

  // reposition of point q
  private Tensor quinte(Tensor p, Tensor q, Tensor r) {
    return midpoint( //
        geodesicSpace.split(p, q, _5_8), //
        geodesicSpace.split(q, r, _3_8));
  }

  // insertion between points q and r
  private Tensor center(Tensor p, Tensor q, Tensor r, Tensor s) {
    return midpoint( //
        geodesicSpace.split(p, q, _15_16), //
        geodesicSpace.split(r, s, _1_16));
  }
}
