// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.ext.Cache;

public enum AveragingWeights implements Genesis {
  INSTANCE;

  private static final int MAX_SIZE = 24;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(AveragingWeights::build, MAX_SIZE);

  private static Tensor build(int n) {
    return ConstantArray.of(RationalScalar.of(1, n), n).unmodifiable();
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return CACHE.apply(levers.length());
  }

  public static Tensor of(int length) {
    return CACHE.apply(length);
  }
}