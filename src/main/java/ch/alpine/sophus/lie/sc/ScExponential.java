// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Exp;
import ch.alpine.tensor.sca.Log;

public enum ScExponential implements Exponential {
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