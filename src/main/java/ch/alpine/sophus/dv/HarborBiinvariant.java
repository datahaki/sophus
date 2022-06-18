// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.FrobeniusNorm;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
/* package */ class HarborBiinvariant extends MatrixBiinvariant {
  public HarborBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return FrobeniusNorm.between(p, q);
  }
}
