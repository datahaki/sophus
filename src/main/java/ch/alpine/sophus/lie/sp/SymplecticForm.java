// code by jph
package ch.alpine.sophus.lie.sp;

import java.io.Serializable;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.spa.SparseArray;

public class SymplecticForm implements Serializable {
  private static final int SIZE = 16;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(SymplecticForm::build, SIZE);

  private static Tensor build(int n) {
    Tensor omega = SparseArray.of(RealScalar.ZERO, 2 * n, 2 * n);
    for (int k = 0; k < n; ++k) {
      omega.set(RealScalar.ONE, k, n + k);
      omega.set(RealScalar.ONE.negate(), n + k, k);
    }
    return omega;
  }

  private final Tensor omega;

  public SymplecticForm(int n) {
    this.omega = CACHE.apply(n);
  }

  public Tensor matrix() {
    return omega;
  }
}
