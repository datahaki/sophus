// code by jph
package ch.alpine.sophus.fit;

import java.util.stream.IntStream;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sqrt;

/** Reference:
 * IV Distance Matrices
 * "Linear Algebra Learning from Data"
 * by Gilbert Strang, 2019 */
public class DistanceMatrixToPoints {
  /** @param matrix symmetric of squared distances
   * @return list of n points of minimal dimension centered around origin
   * @see SymmetricMatrixQ */
  public static Tensor of(Tensor matrix) {
    return of(matrix, Tolerance.CHOP);
  }

  /** @param matrix symmetric of squared distances
   * @param chop
   * @return list of n points of minimal dimension centered around origin
   * @see SymmetricMatrixQ */
  public static Tensor of(Tensor matrix, Chop chop) {
    SymmetricMatrixQ.require(matrix, chop);
    int n = matrix.length();
    Tensor d0 = matrix.get(0);
    Tensor ones = ConstantArray.of(RealScalar.ONE, n);
    Tensor g = TensorProduct.of(ones, d0).add(TensorProduct.of(d0, ones)) //
        .subtract(matrix).multiply(RationalScalar.HALF);
    Eigensystem eigensystem = Eigensystem.ofSymmetric(g);
    Tensor sqrt = eigensystem.values().map(chop).map(Sqrt.FUNCTION);
    Tensor vectors = eigensystem.vectors();
    Tensor x = Transpose.of(Tensor.of(IntStream.range(0, n) //
        .filter(i -> Scalars.nonZero(sqrt.Get(i))) //
        .mapToObj(i -> vectors.get(i).multiply(sqrt.Get(i)))));
    Tensor mean = Mean.of(x).negate();
    return Tensor.of(x.stream().map(mean::add));
  }
}
