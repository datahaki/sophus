// code by mh, jph
package ch.alpine.sophus.flt.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

/** @see Regularization2Step */
/* package */ class Regularization2StepString extends Regularization2Step {
  public Regularization2StepString(GeodesicSpace geodesicSpace, Scalar factor) {
    super(geodesicSpace, factor);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    if (tensor.length() < 2) {
      ScalarQ.thenThrow(tensor);
      return tensor.copy();
    }
    List<Tensor> list = new ArrayList<>(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    Tensor curr = iterator.next();
    list.add(prev.copy());
    while (iterator.hasNext()) {
      Tensor next = iterator.next();
      list.add(average(prev, curr, next));
      prev = curr;
      curr = next;
    }
    list.add(curr.copy());
    Integers.requireEquals(list.size(), tensor.length());
    return Unprotect.using(list);
  }
}
