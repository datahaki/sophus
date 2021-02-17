// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.gl.GlGroup;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

/** a group element of SE(3) is represented as a 4x4 affine transformation matrix
 * 
 * an element of the algebra se(3) is represented as a 2 x 3 matrix of the form
 * {{vx, vy, vz}, {wx, wy, wz}}
 * 
 * from "Lie Groups for 2D and 3D Transformations" by Ethan Eade
 * http://ethaneade.com/
 * 
 * @see GlGroup
 * @see LieGroupElement */
public enum Se3Exponential implements Exponential {
  INSTANCE;

  private static final Tensor ID3 = IdentityMatrix.of(3);

  @Override // from Exponential
  public Tensor exp(Tensor u_w) {
    Tensor u = u_w.get(0); // translation
    Tensor w = u_w.get(1); // rotation
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
    Tensor R = Se3Matrix.rotation(g);
    Tensor wx = Rodrigues.INSTANCE.log(R);
    Tensor w = Rodrigues.vectorize(wx);
    Scalar theta = Vector2Norm.of(w);
    Tensor wx2 = wx.dot(wx);
    Se3Numerics se3Numerics = new Se3Numerics(theta);
    Tensor Vi = ID3.subtract(wx.multiply(RationalScalar.HALF)).add(wx2.multiply(se3Numerics.D));
    Tensor t = Se3Matrix.translation(g);
    return Tensors.of(Vi.dot(t), w);
  }

  /** @param g matrix of dimensions 4 x 4
   * @return vector of length 6 */
  @Override // from TangentSpace
  public Tensor vectorLog(Tensor g) {
    return Flatten.of(log(g));
  }
}
