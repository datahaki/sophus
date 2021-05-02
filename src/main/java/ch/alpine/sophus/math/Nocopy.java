// code by jph
package ch.alpine.sophus.math;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;

public final class Nocopy {
  private final List<Tensor> list;

  public Nocopy(int initialCapacity) {
    list = new ArrayList<>(initialCapacity);
  }

  public Nocopy append(Tensor tensor) {
    list.add(tensor);
    return this;
  }

  public Tensor tensor() {
    return Unprotect.using(list);
  }
}
