// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.sn.SnExponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.lie.Orthogonalize;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** 2-dimensional tangent space at given point of 2-dimensional sphere
 * i.e. tangent space not embedded in R^3 */
public class S2TangentSpace implements TangentSpace, Serializable {
  private static final Tensor ID3 = IdentityMatrix.of(3);
  // ---
  private final TangentSpace tangentSpace;
  private final Tensor project;

  public S2TangentSpace(Tensor xyz) {
    tangentSpace = new SnExponential(xyz);
    project = Orthogonalize.of(Join.of(Tensors.of(xyz), ID3)).extract(1, 3);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return project.dot(tangentSpace.vectorLog(q));
  }
}
