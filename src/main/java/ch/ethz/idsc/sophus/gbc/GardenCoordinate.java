// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class GardenCoordinate implements BarycentricCoordinate, Serializable {
  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param vectorLogManifold
   * @param target operator with point as input parameter
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    return new GardenCoordinate(vectorLogManifold, target);
  }

  /***************************************************/
  private final HsLevers hsLevers;
  private final TensorUnaryOperator target;

  private GardenCoordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    hsLevers = new HsLevers(vectorLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = hsLevers.levers(sequence, point);
    Tensor nullsp = LeftNullSpace.usingQR(levers);
    Tensor vector = NormalizeTotal.FUNCTION.apply(target.apply(point));
    return NormalizeTotal.FUNCTION.apply(nullsp.dot(vector).dot(nullsp));
  }
}
