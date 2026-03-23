// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.bm.AffineVectorQ;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Int;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.pow.Power;

// ONLY USED FOR TESTING
public enum WeightedGeometricMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineVectorQ.INSTANCE.require(weights);
    Int i = new Int();
    return sequence.stream() //
        .map(point -> point.maps(Power.function(weights.Get(i.getAndIncrement())))) //
        .reduce(Times::of).orElseThrow();
  }
}
