// code by jph
package ch.alpine.sophus.hs.s2;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.sn.SnTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** 2-dimensional tangent space parameterization */
public enum S2Manifold implements HomogeneousSpace {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor p) {
    return new S2Exponential(p);
  }

  @Override
  public HsTransport hsTransport() {
    return SnTransport.INSTANCE;
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    throw new UnsupportedOperationException();
  }
}
