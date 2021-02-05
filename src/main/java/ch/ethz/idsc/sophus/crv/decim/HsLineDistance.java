// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.red.Norm;

public class HsLineDistance implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final VectorLogManifold vectorLogManifold;

  public HsLineDistance(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor beg, Tensor end) {
    TangentSpace vectorLog = vectorLogManifold.logAt(beg);
    return new NormImpl( //
        vectorLog, //
        NORMALIZE_UNLESS_ZERO.apply(vectorLog.vectorLog(end)));
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
      return vector.dot(normal).pmul(normal);
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor orthogonal(Tensor tensor) {
      Tensor vector = tangentSpace.vectorLog(tensor); // redundant to project
      return vector.subtract(vector.dot(normal).pmul(normal)); // ... but vector has to be stored
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      return Hypot.ofVector(orthogonal(tensor));
    }
  }
}
