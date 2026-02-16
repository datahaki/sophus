// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.pow.Sqrt;

/** intended for the use on output of {@link LowerVectorize} with diagonal elements
 * 
 * provided that symmetric_matrix is a symmetric matrix, the following identity holds:
 * <pre>
 * FrobeniusNorm[symmetric_matrix] == LowerVectorize0_2Norm[LowerVectorize[symmetric_matrix, 0]]
 * </pre>
 * 
 * @see FrobeniusNorm */
public enum LowerVectorize0_2Norm implements TensorNorm {
  INSTANCE;

  private static final Scalar SQRT2 = Sqrt.FUNCTION.apply(RealScalar.TWO);

  @Override
  public Scalar norm(Tensor vector) {
    Tensor vn = Tensors.reserve(requireTriangleNumber(vector.length()));
    int index = 0;
    int next = 0;
    int skip = 0;
    for (Tensor value : vector) {
      if (index == next)
        next += ++skip;
      vn.append(++index == next ? value : value.multiply(SQRT2));
    }
    return Vector2Norm.of(vn);
  }

  /** @param n */
  @PackageTestAccess
  static int requireTriangleNumber(int n) {
    ExactScalarQ.require(Sqrt.FUNCTION.apply(RealScalar.of(1 + 8 * n)).subtract(RealScalar.ONE).multiply(Rational.HALF));
    return n;
  }
}
