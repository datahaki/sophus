// code by jph
package ch.alpine.sophus.fit;

import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Orthogonalize;
import ch.alpine.tensor.red.Times;

/** function computes the best-fitting rigid transformation that aligns
 * two sets of corresponding n elements from d-dimensional vector space.
 * 
 * Reference:
 * "Least-Squares Rigid Motion Using SVD"
 * Olga Sorkine-Hornung and Michael Rabinovich, 2016 */
public class RigidMotionFit implements TensorUnaryOperator {
  /** @param origin matrix of dimension n x d
   * @param target matrix of dimension n x d
   * @param weights vector of length n with entries that sum up to 1
   * @return
   * @throws Exception if total of weights does not equal 1 */
  public static RigidMotionFit of(Tensor origin, Tensor target, Tensor weights) {
    AffineQ.require(weights);
    return _of(origin, target, weights);
  }

  /** @param origin matrix of dimension n x d
   * @param target matrix of dimension n x d
   * @return */
  public static RigidMotionFit of(Tensor origin, Tensor target) {
    return _of(origin, target, AveragingWeights.of(origin.length()));
  }

  // helper function
  private static RigidMotionFit _of(Tensor origin, Tensor target, Tensor weights) {
    Tensor pm = weights.dot(origin); // weighted mean of origin coordinates
    Tensor qm = weights.dot(target); // weighted mean of target coordinates
    Tensor xt = Tensor.of(origin.stream().map(pm.negate()::add)); // levers to origin coordinates
    Tensor yt = Tensor.of(target.stream().map(qm.negate()::add)); // levers to target coordinates
    Tensor rotation = Orthogonalize.usingSvd(Transpose.of(yt).dot(Times.of(weights,xt)));
    return new RigidMotionFit(rotation, qm.subtract(rotation.dot(pm)));
  }

  // ---
  private final Tensor rotation;
  private final Tensor translation;

  private RigidMotionFit(Tensor rotation, Tensor translation) {
    this.rotation = rotation;
    this.translation = translation;
  }

  /** rotation dot point plus translation == target
   * 
   * @return orthogonal matrix with dimension d x d and determinant +1 */
  public Tensor rotation() {
    return rotation;
  }

  /** rotation dot point plus translation == target
   * 
   * @return vector of length d */
  public Tensor translation() {
    return translation;
  }

  @Override
  public Tensor apply(Tensor point) {
    return rotation.dot(point).add(translation);
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), Tensors.message(rotation(), translation()));
  }
}
