// code by jph
package ch.ethz.idsc.sophus.hs.h2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.hn.HnAngle;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;

public class H2TangentSpace implements TangentSpace, Serializable {
  private final HnAngle hnAngle;

  /** @param x vector of length 3 */
  public H2TangentSpace(Tensor x) {
    VectorQ.requireLength(x, 3);
    hnAngle = new HnAngle(x);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return hnAngle.vectorLog(y);
  }
}
