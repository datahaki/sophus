// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

public record HsLineDistance(HomogeneousSpace hsManifold) implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    Exponential exponential = hsManifold.exponential(p);
    return new NormImpl( //
        exponential, //
        NORMALIZE_UNLESS_ZERO.apply(exponential.vectorLog(q)));
  }

  // TODO SOPHUS API probably should extract?
  public static record NormImpl(Exponential exponential, Tensor normal) implements TensorNorm, Serializable {
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
}
