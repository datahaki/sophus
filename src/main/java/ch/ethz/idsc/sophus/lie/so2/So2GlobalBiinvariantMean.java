// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.lie.ScalarBiinvariantMean;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** biinvariant mean defined globally on SO(2) for arbitrary weights
 * 
 * invariant under simultaneous permutation of control point sequence and weight vector
 * 
 * elements of SO(2) are represented as scalars
 * 
 * Reference:
 * "Illumination for computer generated pictures"
 * by Bui Tuong Phong, 1975 */
public enum So2GlobalBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    return ArcTan2D.of(weights.dot(sequence.map(AngleVector::of)));
  }
}
