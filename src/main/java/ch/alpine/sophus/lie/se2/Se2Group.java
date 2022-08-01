// code by jph
package ch.alpine.sophus.lie.se2;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** parameterized by R^2 x [-pi, pi)
 * 
 * References:
 * http://vixra.org/abs/1807.0463
 * https://www.youtube.com/watch?v=2vDciaUgL4E */
public class Se2Group extends AbstractSe2Group implements Serializable {
  public static final Se2Group INSTANCE = new Se2Group();

  private Se2Group() {
  }

  @Override // from LieGroup
  public Se2GroupElement element(Tensor xya) {
    return new Se2GroupElement(xya);
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return Se2BiinvariantMeans.LINEAR;
  }
}
