// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;

import ch.alpine.sophus.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.red.Trace;

/** This is called the:
 * 
 * Affine-invariant metric
 * Fisher–Rao metric (in statistics)
 * Cartan/Killing–induced metric on the symmetric space. */
/* package */ class SpdBilinearForm implements BilinearForm, Serializable {
  private final Tensor pinv;

  public SpdBilinearForm(Tensor p) {
    pinv = Inverse.of(p);
  }

  @Override
  public Scalar formEval(Tensor u, Tensor v) {
    return Trace.of(Dot.of(pinv, u, pinv, v));
  }
}
