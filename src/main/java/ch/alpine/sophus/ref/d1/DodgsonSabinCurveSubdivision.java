// code by jph
// adapted from document by Tobias Ewald
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;

/** 2005 Malcolm A. Sabin, Neil A. Dodgson:
 * A Circle-Preserving Variant of the Four-Point Subdivision Scheme
 * 
 * reproduces circles
 * 
 * control points are in R^2
 * 
 * subdivision along geodesics in metric spaces other than Euclidean is not defined */
public enum DodgsonSabinCurveSubdivision implements CurveSubdivision {
  INSTANCE;

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(2 * length);
    for (int index = 0; index < length; ++index) {
      list.add(tensor.get(index));
      Tensor a = tensor.get((index - 1 + tensor.length()) % tensor.length());
      Tensor b = tensor.get((index + 0 + tensor.length()) % tensor.length());
      Tensor c = tensor.get((index + 1 + tensor.length()) % tensor.length());
      Tensor d = tensor.get((index + 2 + tensor.length()) % tensor.length());
      list.add(DodgsonSabinHelper.midpoint(a, b, c, d));
    }
    return Unprotect.using(list);
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    int length = tensor.length();
    int last = length - 1;
    if (last < 2)
      return DodgsonSabinHelper.BSPLINE3_EUCLIDEAN.string(tensor);
    // ---
    List<Tensor> list = new ArrayList<>(2 * length);
    list.add(tensor.get(0));
    list.add(DodgsonSabinHelper.midpoint(tensor.get(0), tensor.get(1), tensor.get(2)));
    // ---
    for (int index = 1; index < last - 1; ++index) {
      list.add(tensor.get(index));
      list.add(DodgsonSabinHelper.midpoint( //
          tensor.get(index - 1), //
          tensor.get(index + 0), //
          tensor.get(index + 1), //
          tensor.get(index + 2)));
    }
    list.add(tensor.get(last - 1));
    list.add(DodgsonSabinHelper.midpoint(tensor.get(last), tensor.get(last - 1), tensor.get(last - 2)));
    list.add(tensor.get(last));
    return Unprotect.using(list);
  }
}
