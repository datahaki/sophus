// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.so.SoGroupElement;
import ch.alpine.sophus.lie.so.SoPhongMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

/** special orthogonal group of 3 x 3 orthogonal matrices with determinant 1
 * 
 * elements from the group SO(3) are 3x3 matrices
 * elements from the tangent space TeSO(3) are skew 3x3 matrices
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.3, 2012
 * 
 * left-invariant Riemannian distance on SO(3)
 * 
 * Reference:
 * "Computing the Mean of Geometric Features Application to the Mean Rotation"
 * by Xavier Pennec, 1998 */
public enum So3Group implements LieGroup, MetricManifold {
  INSTANCE;

  @Override // from LieGroup
  public SoGroupElement element(Tensor matrix) {
    return SoGroupElement.of(matrix);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) { // v is vector of length 3
    return Rodrigues.vectorExp(v);
  }

  @Override // from Exponential
  public Tensor log(Tensor p) {
    return Rodrigues.INSTANCE.vectorLog(p);
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor q) {
    return Rodrigues.INSTANCE.vectorLog(q);
  }

  /** p and q are orthogonal matrices with dimension 3 x 3 */
  @Override // from GeodesicSpace
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = Rodrigues.INSTANCE.log(LinearSolve.of(p, q));
    return scalar -> p.dot(Rodrigues.INSTANCE.exp(log.multiply(scalar)));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.of(So3Group.INSTANCE, chop, SoPhongMean.INSTANCE);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Vector2Norm.of(Rodrigues.INSTANCE.vectorLog(LinearSolve.of(p, q)));
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor v) {
    return Vector2Norm.of(v);
  }
}
