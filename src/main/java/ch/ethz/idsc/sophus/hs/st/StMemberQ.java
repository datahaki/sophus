// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractHsMemberQ;
import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class StMemberQ extends AbstractHsMemberQ {
  /** @param chop
   * @return */
  public static HsMemberQ of(Chop chop) {
    return new StMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private StMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override
  public boolean isPoint(Tensor x) {
    return OrthogonalMatrixQ.of(x, chop);
  }

  @Override
  public boolean isTangent(Tensor x, Tensor v) {
    // TODO tangent in stiefel manifold
    throw TensorRuntimeException.of(x, v);
  }
}
