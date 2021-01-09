// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.sn.SnExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Orthogonalize;

/** 2-dimensional tangent space at given point of 2-dimensional sphere
 * i.e. tangent space not embedded in R^3 */
public class S2Exponential implements Exponential, TangentSpace, Serializable {
  private static final long serialVersionUID = -305759923743177212L;
  private static final Tensor ID3 = IdentityMatrix.of(3);
  // ---
  private final Exponential exponential;
  private final Tensor project;

  /** @param xyz vector of the form {x, y, z} with unit length */
  public S2Exponential(Tensor xyz) {
    exponential = new SnExponential(xyz);
    project = Orthogonalize.of(Join.of(Tensors.of(xyz), ID3)).extract(1, 3);
  }

  @Override // from Exponential
  public Tensor exp(Tensor vector) {
    return exponential.exp(vector.dot(project));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return project.dot(exponential.log(q));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return log(q);
  }

  /** @return 2 x 3 matrix */
  public Tensor projection() {
    return project;
  }
}
