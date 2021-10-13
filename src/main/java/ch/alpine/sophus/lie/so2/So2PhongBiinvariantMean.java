// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.lie.ScalarBiinvariantMean;
import ch.alpine.sophus.math.ArcTan2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.AngleVector;

/** biinvariant mean defined globally on SO(2) for arbitrary weights
 * 
 * invariant under simultaneous permutation of control point sequence and weight vector
 * 
 * elements of SO(2) are represented as scalars
 * 
 * Reference:
 * "Illumination for computer generated pictures"
 * by Bui Tuong Phong, 1975 */
public enum So2PhongBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    return ArcTan2D.of(weights.dot(sequence.map(AngleVector::of)));
  }
}
