// code by mh, jph
package ch.alpine.sophus.flt.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.alpine.sophus.math.SplitInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ScalarQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;

/** @see Regularization2Step */
/* package */ class Regularization2StepCyclic extends Regularization2Step {
  public Regularization2StepCyclic(SplitInterface splitInterface, Scalar factor) {
    super(splitInterface, factor);
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
    list.add(average(Last.of(tensor), prev, curr));
    while (iterator.hasNext()) {
      Tensor next = iterator.next();
      list.add(average(prev, curr, next));
      prev = curr;
      curr = next;
    }
    list.add(average(prev, curr, tensor.get(0)));
    return Unprotect.using(list);
  }
}
