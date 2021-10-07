// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.Integers;

/** examples of extensions are
 * {@link BSpline3CurveSubdivision}, and
 * {@link MSpline3CurveSubdivision} */
public abstract class RefiningBSpline3CurveSubdivision extends AbstractBSpline3CurveSubdivision {
  @Override // from AbstractBSpline3CurveSubdivision
  protected final Tensor refine(Tensor tensor) {
    int length = tensor.length();
    int capacity = 2 * length - 1;
    List<Tensor> list = new ArrayList<>(capacity);
    {
      Tensor q = tensor.get(0);
      Tensor r = tensor.get(1);
      list.add(q);
      list.add(midpoint(q, r));
    }
    int last = length - 1;
    Tensor p = tensor.get(0);
    for (int index = 1; index < last; /* nothing */ ) {
      Tensor q = tensor.get(index);
      Tensor r = tensor.get(++index);
      list.add(center(p, q, r));
      list.add(midpoint(q, r));
      p = q;
    }
    list.add(tensor.get(last));
    Integers.requireEquals(list.size(), capacity);
    return Unprotect.using(list);
  }
}
