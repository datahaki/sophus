// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.hn.HnExponential;
import ch.ethz.idsc.sophus.hs.hn.HnMetric;
import ch.ethz.idsc.sophus.math.Extract2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Hypot;

public class H2TangentSpace implements TangentSpace, Serializable {
  private final HnExponential hnExponential;
  private final Tensor p;

  public H2TangentSpace(Tensor p) {
    hnExponential = new HnExponential(p);
    this.p = p;
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // TODO this is not elegant
    Scalar distance = HnMetric.INSTANCE.distance(p, q);
    Tensor log = Extract2D.FUNCTION.apply(hnExponential.vectorLog(q));
    return log.multiply(distance.divide(Hypot.ofVector(log)));
  }
}
