// code by ob
package ch.ethz.idsc.sophus.hs.r2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Hypot;

/** the angular component of q has no influence on the outcome.
 * therefore, it is more appropriate to think of the distance as the
 * "distance" of the action of p in SE(2) and q in R^2
 * 
 * Careful: Se2CoveringParametric is <em>not<em> a metric!
 * 
 * @see Se2Parametric */
public class Se2CoveringParametric implements TensorMetric, Serializable {
  public static final TensorMetric INSTANCE = new Se2CoveringParametric(Se2CoveringGroup.INSTANCE);
  /***************************************************/
  private final LieGroup lieGroup;

  /* package */ Se2CoveringParametric(LieGroup lieGroup) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
  }

  /** measurement based on degenerate bilinear form [1 1 0]
   * 
   * @param p element in SE2 of the form {px, py, p_heading}
   * @param q element in SE2 of the form {qx, qy, q_heading}
   * @return length of geodesic between p and q when projected to R^2 including the number of windings */
  @Override // from TensorMetric
  public final Scalar distance(Tensor p, Tensor q) {
    Tensor log = Se2CoveringExponential.INSTANCE.log(lieGroup.element(p).inverse().combine(q));
    return Hypot.of(log.Get(0), log.Get(1));
  }
}
