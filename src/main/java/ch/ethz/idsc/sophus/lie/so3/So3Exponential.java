// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;

public class So3Exponential implements Exponential, FlattenLog, Serializable {
  private final Tensor p;
  private final Tensor pinv;

  public So3Exponential(Tensor p) {
    this.p = p;
    pinv = Inverse.of(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor vp) { // tangent vector at p
    return p.dot(Rodrigues.INSTANCE.exp(pinv.dot(vp)));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return p.dot(Rodrigues.INSTANCE.log(pinv.dot(q)));
  }

  @Override // from FlattenLog
  public Tensor flattenLog(Tensor q) {
    return Rodrigues.INSTANCE.flattenLog(pinv.dot(q));
  }
}
