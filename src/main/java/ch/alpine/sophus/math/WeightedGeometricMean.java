// code by jph
package ch.alpine.sophus.math;

import java.util.concurrent.atomic.AtomicInteger;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Power;

public enum WeightedGeometricMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    AtomicInteger atomicInteger = new AtomicInteger();
    return sequence.stream() //
        .map(point -> point.map(Power.function(weights.Get(atomicInteger.getAndIncrement())))) //
        .reduce(Tensor::pmul).orElseThrow();
  }
}
