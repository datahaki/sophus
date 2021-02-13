// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.ext.Cache;

public enum AveragingWeights implements Genesis {
  INSTANCE;

  private static final int MAX_SIZE = 16;
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