// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Log;

public enum ScExponential implements LieExponential {
  INSTANCE;

  @Override // from LieExponential
  public Tensor exp(Tensor x) {
    return x.map(Exp.FUNCTION);
  }

  @Override // from LieExponential
  public Tensor log(Tensor g) {
    return g.map(Log.FUNCTION);
  }
}
