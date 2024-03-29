// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

/** g is a 4 x 4 matrix in SE(3)
 * 
 * a group element of SE(3) is represented as a 4x4 affine transformation matrix
 * 
 * an element of the algebra se(3) is represented as a 2 x 3 matrix of the form
 * {{vx, vy, vz}, {wx, wy, wz}}
 * 
 * from "Lie Groups for 2D and 3D Transformations" by Ethan Eade
 * http://ethaneade.com/
 * 
 * geodesic in special Euclidean group SE(3) of affine transformations
 * 
 * input p and q are 4 x 4 matrices that encode affine transformations
 * 
 * @see GlGroup
 * @see LieGroupElement */
public enum Se3Group implements LieGroup {
  INSTANCE;

  /** @param g element from Lie Group SE3 as 4x4 affine matrix
   * @return */
  @Override // from LieGroup
  public Se3GroupElement element(Tensor g) {
    return new Se3GroupElement(g);
  }

  private static final Tensor ID3 = IdentityMatrix.of(3);

  @Override // from Exponential
  public Tensor exp(Tensor u_w) {
    VectorQ.requireLength(u_w, 6);
    Tensor u = u_w.extract(0, 3); // translation
    Tensor w = u_w.extract(3, 6); // rotation
    Scalar theta = Vector2Norm.of(w);
    Tensor wx = Cross.skew3(w);
    Tensor wx2 = wx.dot(wx);
    Se3Numerics se3Numerics = new Se3Numerics(theta);
    Tensor R = ID3.add(wx.multiply(se3Numerics.A)).add(wx2.multiply(se3Numerics.B));
    Tensor V = ID3.add(wx.multiply(se3Numerics.B)).add(wx2.multiply(se3Numerics.C));
    Tensor Vu = V.dot(u);
    return Se3Matrix.of(R, Vu);
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    return vectorLog(g);
  }

  /** @param g matrix of dimensions 4 x 4
   * @return vector of length 6 */
  @Override // from Exponential
  public Tensor vectorLog(Tensor g) {
    Tensor R = Se3Matrix.rotation(g);
    Tensor wx = Rodrigues.INSTANCE.log(R);
    Tensor w = Rodrigues.vectorize(wx);
    Scalar theta = Vector2Norm.of(w);
    Tensor wx2 = wx.dot(wx);
    Se3Numerics se3Numerics = new Se3Numerics(theta);
    Tensor Vi = ID3.subtract(wx.multiply(RationalScalar.HALF)).add(wx2.multiply(se3Numerics.D));
    Tensor t = Se3Matrix.translation(g);
    return Join.of(Vi.dot(t), w);
  }

  @Override // from GeodesicSpace
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = log(LinearSolve.of(p, q));
    return scalar -> p.dot(exp(log.multiply(scalar)));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.reduce(this, chop);
  }
}
