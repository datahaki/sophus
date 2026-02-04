// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.io.MathematicaFormat;

/** parameterized by R^2 x [-pi, pi)
 * 
 * References:
 * http://vixra.org/abs/1807.0463
 * https://www.youtube.com/watch?v=2vDciaUgL4E */
public class Se2Group extends Se2CoveringGroup {
  public static final Se2Group INSTANCE = new Se2Group();

  private Se2Group() {
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return Se2BiinvariantMeans.LINEAR;
  }

  @Override
  protected Scalar truncate(Scalar a) {
    return So2.MOD.apply(a);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SE", 2);
  }
}
