// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Log;

public enum ScGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public ScGroupElement element(Tensor tensor) {
    return ScGroupElement.of(tensor);
  }

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

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return ScBiinvariantMean.INSTANCE;
  }
}
