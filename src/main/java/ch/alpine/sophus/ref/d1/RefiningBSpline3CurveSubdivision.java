// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;

/** examples of extensions are
 * {@link BSpline3CurveSubdivision}, and
 * {@link MSpline3CurveSubdivision} */
public abstract class RefiningBSpline3CurveSubdivision extends AbstractBSpline3CurveSubdivision {
  // TODO only called from string refinement -> reserve allocation, class structure
  @Override
  protected final Tensor refine(Tensor tensor) {
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(2 * length);
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
    return Unprotect.using(list);
  }
}
