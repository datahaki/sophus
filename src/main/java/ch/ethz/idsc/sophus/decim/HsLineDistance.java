// code by jph
package ch.ethz.idsc.sophus.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

public class HsLineDistance implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final HsManifold hsManifold;

  public HsLineDistance(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    Exponential exponential = hsManifold.exponential(p);
    return new NormImpl( //
        exponential, //
        NORMALIZE_UNLESS_ZERO.apply(exponential.vectorLog(q)));
  }

  public class NormImpl implements TensorNorm, Serializable {
    private final Exponential exponential;
    private final Tensor normal;

    public NormImpl(Exponential exponential, Tensor normal) {
      this.exponential = exponential;
      this.normal = normal;
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor project(Tensor tensor) {
      Tensor vector = exponential.vectorLog(tensor);
      return vector.dot(normal).pmul(normal);
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor orthogonal(Tensor tensor) {
      Tensor vector = exponential.vectorLog(tensor); // redundant to project
      return vector.subtract(vector.dot(normal).pmul(normal)); // ... but vector has to be stored
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      return Vector2Norm.of(orthogonal(tensor));
    }
  }
}
