// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Log;

public enum ScExponential implements Exponential, TangentSpace {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    return x.map(Exp.FUNCTION);
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    return g.map(Log.FUNCTION);
  }

  @Override
  public Tensor vectorLog(Tensor g) {
    return log(g);
  }
}
