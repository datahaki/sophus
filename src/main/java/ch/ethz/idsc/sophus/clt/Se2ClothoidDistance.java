// code by gjoel
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** EXPERIMENTAL
 * 
 * length of clothoid in Euclidean plane */
public enum Se2ClothoidDistance implements TensorMetric, TensorNorm {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Se2ClothoidBuilder.INSTANCE.curve(p, q).length();
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor xya) {
    return distance(xya.map(Scalar::zero), xya);
  }
}
