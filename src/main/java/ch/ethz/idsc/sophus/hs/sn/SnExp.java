// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Projection;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.VectorAngle;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sinc;

/** Exponential map of S^n manifold at given point
 * 
 * The "orthogonal" property is established by using the dot product in R^(n+1)
 * 
 * function exp throws an exception if input x is not orthogonal to given point
 * function log returns vector orthogonal to x when using the dot product in R^(n+1)
 * 
 * implementation is based on the function "strans" taken from
 * "Freeform Curves on Spheres of Arbitrary Dimension"
 * by Scott Schaefer and Ron Goldman, 2005, page 5 */
public class SnExp implements LieExponential, FlattenLog, Serializable {
  private static final TensorUnaryOperator NORMALIZE = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final Tensor point;
  private final TensorUnaryOperator projection;

  /** @param point on S^n
   * @throws Exception if p is not a vector of Euclidean norm 1 */
  public SnExp(Tensor point) {
    this.point = requireNorm1(point);
    projection = Projection.on(point);
    if (point.length() < 2)
      throw TensorRuntimeException.of(point);
  }

  @Override // from LieExponential
  public Tensor exp(Tensor x) {
    // x is orthogonal to base point
    Chop._07.requireZero(point.dot(x).Get()); // errors of up to 1E-9 are expected
    Scalar norm = Norm._2.ofVector(x);
    return point.multiply(Cos.FUNCTION.apply(norm)).add(x.multiply(Sinc.FUNCTION.apply(norm)));
  }

  @Override // from LieExponential
  public Tensor log(Tensor q) {
    requireNorm1(q);
    return NORMALIZE.apply(q.subtract(projection.apply(q))).multiply(VectorAngle.of(point, q).get());
  }

  @Override
  public Tensor flattenLog(Tensor q) {
    return log(q);
  }

  // helper function
  private static Tensor requireNorm1(Tensor g) {
    Tolerance.CHOP.requireZero(Norm._2.ofVector(g).subtract(RealScalar.ONE));
    return g;
  }
}
