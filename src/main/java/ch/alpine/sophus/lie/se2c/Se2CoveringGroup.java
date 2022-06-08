// code by jph
package ch.alpine.sophus.lie.se2c;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.se2.AbstractSe2Group;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** the covering group of SE(2) is parameterized by R^3
 * 
 * References:
 * http://vixra.org/abs/1807.0463
 * https://www.youtube.com/watch?v=2vDciaUgL4E
 * 
 * @see Se2Skew */
public class Se2CoveringGroup extends AbstractSe2Group implements Serializable {
  public static final Se2CoveringGroup INSTANCE = new Se2CoveringGroup();

  private Se2CoveringGroup() {
  }

  @Override // from LieGroup
  public Se2CoveringGroupElement element(Tensor xya) {
    return new Se2CoveringGroupElement(xya);
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return Se2CoveringBiinvariantMean.INSTANCE;
  }
}
