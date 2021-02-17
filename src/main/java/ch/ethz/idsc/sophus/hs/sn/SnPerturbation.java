// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

public class SnPerturbation implements TensorUnaryOperator {
  public static TensorUnaryOperator of(Distribution distribution) {
    return new SnPerturbation(Objects.requireNonNull(distribution));
  }

  // ---
  private final Distribution distribution;

  private SnPerturbation(Distribution distribution) {
    this.distribution = distribution;
  }

  @Override
  public Tensor apply(Tensor p) {
    SnExponential snExponential = new SnExponential(p);
    Tensor v = RandomSample.of(SnRandomSample.of(p.length() - 2)).multiply(RandomVariate.of(distribution)) //
        .dot(snExponential.tSnProjection);
    return snExponential.exp(v);
  }
}
