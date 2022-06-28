// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

public record HsLineDistanceLocal(Exponential exponential, Tensor normal) implements TensorNorm {
  /** @param tensor of the lie group
   * @return element of the lie algebra */
  public Tensor project(Tensor tensor) {
    Tensor vector = exponential.vectorLog(tensor);
    return Times.of(vector.dot(normal), normal);
  }

  /** @param tensor of the lie group
   * @return element of the lie algebra */
  public Tensor orthogonal(Tensor tensor) {
    Tensor vector = exponential.vectorLog(tensor); // redundant to project
    return vector.subtract(Times.of(vector.dot(normal), normal)); // ... but vector has to be stored
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    return Vector2Norm.of(orthogonal(tensor));
  }
}