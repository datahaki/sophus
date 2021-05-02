// code by jph
package ch.alpine.sophus.itp;

import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.qty.Unit;

/* package */ enum StaticHelper {
  ;
  /** @param tensor
   * @return unit that is common to all scalars in given tensor
   * @throws Exception */
  public static Unit uniqueUnit(Tensor tensor) {
    List<Unit> list = tensor.flatten(-1) //
        .map(Scalar.class::cast) //
        .map(QuantityUnit::of) //
        .distinct() //
        .limit(2) //
        .collect(Collectors.toList());
    if (list.size() == 1)
      return list.get(0);
    throw new IllegalArgumentException(list.toString());
  }
}
