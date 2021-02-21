// code by jph
package ch.ethz.idsc.sophus.math;

import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Power;

public enum WeightedGeometricMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    AtomicInteger atomicInteger = new AtomicInteger();
    return sequence.stream() //
        .map(point -> point.map(Power.function(weights.Get(atomicInteger.getAndIncrement())))) //
        .reduce(Tensor::pmul).get();
  }
}
