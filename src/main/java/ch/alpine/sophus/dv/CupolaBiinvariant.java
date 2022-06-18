// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.gr.GrManifold;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
/* package */ class CupolaBiinvariant extends MatrixBiinvariant {
  public CupolaBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return GrManifold.INSTANCE.distance(p, q);
  }
}
