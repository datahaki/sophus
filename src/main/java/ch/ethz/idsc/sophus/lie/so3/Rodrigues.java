// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.sca.ArcCos;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.N;
import ch.ethz.idsc.tensor.sca.Sinc;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** a group element SO(3) is represented as a 3x3 orthogonal matrix.
 * an element of the algebra so(3) is represented as a vector of length 3
 * 
 * <p>Olinde Rodrigues' formula is a fast and robust method to
 * compute the exponential of a skew symmetric 3x3 matrix.
 * formula taken from Blanes/Casas
 * "A concise introduction to geometric numerical integration"
 * p. 131
 * 
 * <p>The formula for the logarithm is taken from a book by Chirikjian */
public enum Rodrigues implements Exponential {
  INSTANCE;

  private static final Tensor ID3 = N.DOUBLE.of(IdentityMatrix.of(3));
  private static final Scalar HALF = RealScalar.of(0.5);

  @Override // from Exponential
  public Tensor exp(Tensor log) {
    return vectorExp(vectorize(AntisymmetricMatrixQ.require(log, Chop._10)));
  }

  /** @param vector of length 3
   * @return orthogonal matrix with dimensions 3 x 3 */
  public static Tensor vectorExp(Tensor vector) {
    Scalar beta = Vector2Norm.of(vector);
    Scalar s1 = Sinc.FUNCTION.apply(beta);
    Tensor X1 = Cross.skew3(vector.multiply(s1));
    Scalar h2 = Sinc.FUNCTION.apply(beta.multiply(HALF));
    Scalar r2 = Sqrt.FUNCTION.apply(h2.multiply(h2).multiply(HALF));
    Tensor X2 = Cross.skew3(vector.multiply(r2));
    return ID3.add(X1).add(X2.dot(X2));
  }

  /** @param q orthogonal with dimensions 3 x 3
   * @return skew-symmetric 3 x 3 matrix X with exp X == matrix
   * @throws Exception if given matrix is not orthogonal or does not have dimensions 3 x 3
   * @see OrthogonalMatrixQ */
  @Override // from Exponential
  public Tensor log(Tensor q) {
    if (q.length() == 3 && //
        OrthogonalMatrixQ.of(q)) {
      Scalar sinc = Sinc.FUNCTION.apply(theta(q));
      return q.subtract(Transpose.of(q)).divide(sinc.add(sinc));
    }
    throw TensorRuntimeException.of(q);
  }

  private static Scalar theta(Tensor matrix) {
    Scalar value = matrix.Get(0, 0).add(matrix.Get(1, 1)).add(matrix.Get(2, 2)) //
        .subtract(RealScalar.ONE).multiply(HALF);
    return ArcCos.FUNCTION.apply(value);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return vectorize(log(q));
  }

  /** @param log 3 x 3 matrix in so(3)
   * @return vector of length 3 */
  public static Tensor vectorize(Tensor log) {
    return Tensors.of(log.Get(2, 1), log.Get(0, 2), log.Get(1, 0));
  }
}
