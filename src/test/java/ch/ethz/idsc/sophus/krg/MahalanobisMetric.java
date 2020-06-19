// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Careful: This is not a metric! */
public class MahalanobisMetric implements TensorMetric, Serializable {
  private final VectorLogManifold vectorLogManifold;
  private final Mahalanobis mahalanobis;
  private final Tensor sequence;

  public MahalanobisMetric(VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.sequence = sequence;
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Tensor sigma_inverse = mahalanobis.new Form(sequence, p).sigma_inverse();
    Tensor log = vectorLogManifold.logAt(p).vectorLog(q);
    return Sqrt.FUNCTION.apply(sigma_inverse.dot(log).dot(log).Get());
  }
}
