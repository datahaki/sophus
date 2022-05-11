// code by jph
package ch.alpine.sophus.hs.s2;

import java.io.Serializable;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.hs.sn.TSnProjection;
import ch.alpine.tensor.Tensor;

/** 2-dimensional tangent space at given point of 2-dimensional sphere
 * i.e. tangent space not embedded in R^3 */
public class S2Exponential implements Exponential, Serializable {
  private final Exponential exponential;
  private final Tensor project;

  /** @param xyz vector of the form {x, y, z} with unit length */
  public S2Exponential(Tensor xyz) {
    exponential = new SnExponential(xyz);
    project = TSnProjection.of(xyz);
  }

  @Override // from Exponential
  public Tensor exp(Tensor vector) { // input vector of length 2
    return exponential.exp(vector.dot(project));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return project.dot(exponential.log(q)); // returns vector of length 2
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor q) {
    return log(q); // returns vector of length 2
  }

  /** @return 2 x 3 matrix */
  public Tensor projection() {
    return project;
  }
}
