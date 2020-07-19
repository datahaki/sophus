// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: This is not a metric! */
public class MahalanobisMetric implements TensorMetric, Serializable {
  private final VectorLogManifold vectorLogManifold;
  private final Tensor sequence;

  public MahalanobisMetric(VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    this.sequence = sequence;
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return new Mahalanobis(vectorLogManifold.logAt(p), sequence).distance(q);
  }
}
