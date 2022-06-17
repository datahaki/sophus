// code by jph
package ch.alpine.sophus.lie.he;

import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** (2*n+1)-dimensional Heisenberg group */
public enum HeGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public HeGroupElement element(Tensor xyz) {
    return new HeGroupElement(xyz);
  }

  @Override // from Exponential
  public Tensor exp(Tensor uvw) {
    return HeFormat.of(uvw).exp().toCoordinate();
  }

  @Override // from Exponential
  public Tensor log(Tensor xyz) {
    return vectorLog(xyz);
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor xyz) {
    return HeFormat.of(xyz).log().toCoordinate();
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    Objects.requireNonNull(chop);
    return HeBiinvariantMean.INSTANCE;
  }
}
