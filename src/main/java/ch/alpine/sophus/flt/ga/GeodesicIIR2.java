// code by ob
package ch.alpine.sophus.flt.ga;

import java.util.Objects;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.BinaryAverage;

/** filter blends extrapolated value with measurement */
public class GeodesicIIR2 implements TensorUnaryOperator {
  private final BinaryAverage binaryAverage;
  private final Scalar alpha;
  // ---
  private transient Tensor p = null;
  private transient Tensor q = null;

  public GeodesicIIR2(BinaryAverage binaryAverage, Scalar alpha) {
    this.binaryAverage = binaryAverage;
    this.alpha = alpha;
  }

  /** @return extrapolated "best guess" value from the previous predictions */
  private Tensor extrapolate() {
    if (Objects.isNull(p))
      return q;
    return binaryAverage.split(p, q, RealScalar.TWO);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    if (Objects.isNull(q)) {
      q = tensor.copy();
      return q.copy();
    }
    Tensor result = binaryAverage.split(extrapolate(), tensor, alpha);
    p = q;
    q = result.copy();
    return result;
  }
}
