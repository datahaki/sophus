// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.math.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

public class HsLineDistanceOld implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final VectorLogManifold vectorLogManifold;

  public HsLineDistanceOld(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor p, Tensor q) {
    TangentSpace tangentSpace = vectorLogManifold.logAt(p);
    return new NormImpl( //
        tangentSpace, //
        NORMALIZE_UNLESS_ZERO.apply(tangentSpace.vectorLog(q)));
  }

  public class NormImpl implements TensorNorm, Serializable {
    private final TangentSpace tangentSpace;
    private final Tensor normal;

    public NormImpl(TangentSpace tangentSpace, Tensor normal) {
      this.tangentSpace = tangentSpace;
      this.normal = normal;
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor project(Tensor tensor) {
      Tensor vector = tangentSpace.vectorLog(tensor);
      return Times.of(vector.dot(normal), normal);
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor orthogonal(Tensor tensor) {
      Tensor vector = tangentSpace.vectorLog(tensor); // redundant to project
      return vector.subtract(Times.of(vector.dot(normal), normal)); // ... but vector has to be stored
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      return Vector2Norm.of(orthogonal(tensor));
    }
  }
}
