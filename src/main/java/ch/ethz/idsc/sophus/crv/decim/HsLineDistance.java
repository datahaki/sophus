// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

public class HsLineDistance implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final FlattenLogManifold flattenLogManifold;

  public HsLineDistance(FlattenLogManifold flattenLogManifold) {
    this.flattenLogManifold = Objects.requireNonNull(flattenLogManifold);
  }

  @Override // from LineDistance
  public NormImpl tensorNorm(Tensor beg, Tensor end) {
    FlattenLog flattenLog = flattenLogManifold.logAt(beg);
    return new NormImpl( //
        flattenLog, //
        NORMALIZE_UNLESS_ZERO.apply(flattenLog.flattenLog(end)));
  }

  public class NormImpl implements TensorNorm, Serializable {
    private final FlattenLog flattenLog;
    private final Tensor normal;

    public NormImpl(FlattenLog flattenLog, Tensor normal) {
      this.flattenLog = flattenLog;
      this.normal = normal;
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor project(Tensor tensor) {
      Tensor vector = flattenLog.flattenLog(tensor);
      return vector.dot(normal).pmul(normal);
    }

    /** @param tensor of the lie group
     * @return element of the lie algebra */
    public Tensor orthogonal(Tensor tensor) {
      Tensor vector = flattenLog.flattenLog(tensor); // redundant to project
      return vector.subtract(vector.dot(normal).pmul(normal)); // ... but vector has to be stored
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      return Norm._2.ofVector(orthogonal(tensor));
    }
  }
}
