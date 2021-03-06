// code by jph
package ch.ethz.idsc.sophus.ref.d2;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Cos;

/* package */ enum DooSabinWeights implements Function<Integer, Tensor> {
  INSTANCE;

  public static final Function<Integer, Tensor> CACHE = Cache.of(INSTANCE, 3 * 16);

  /* package */ static Tensor numeric(int n) {
    Tensor w = Range.of(1, n).multiply(Pi.TWO).divide(RealScalar.of(n));
    Tensor p = Cos.of(w).multiply(RealScalar.TWO).map(s -> s.add(RealScalar.of(3)));
    return Join.of(Tensors.vector(n + 5), p).divide(RealScalar.of(4 * n));
  }

  private static Tensor vector(int n) {
    switch (n) {
    case 3:
      return Tensors.vector(4, 1, 1).divide(RealScalar.of(6));
    case 4:
      return Tensors.vector(9, 3, 1, 3).divide(RealScalar.of(16));
    case 6:
      return Tensors.vector(11, 4, 2, 1, 2, 4).divide(RealScalar.of(24));
    default:
      return numeric(n);
    }
  }

  @Override
  public Tensor apply(Integer n) {
    return vector(n).unmodifiable();
  }
}
