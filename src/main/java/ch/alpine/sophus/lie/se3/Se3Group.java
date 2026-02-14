// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.sophus.lie.so.Rodrigues;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.lie.rot.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.nrm.Vector2Norm;

/** g is a 4 x 4 affine matrix in SE(3)
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
 * @see GlGroup */
public class Se3Group extends SeNGroup implements VectorEncodingMarker {
  public static final Se3Group INSTANCE = new Se3Group();

  public Se3Group() {
    super(3);
  }

  private static final Tensor ID3 = IdentityMatrix.of(3);

  private enum Exponential0 implements Exponential {
    INSTANCE;

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

    @Override
    public ZeroDefectArrayQ isTangentQ() {
      return VectorQ.ofLength(6);
    }
  }

  @Override
  public Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public Tensor invert(Tensor element) {
    return Se3Matrix.inverse(element);
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor g, Tensor u_w) {
    Tensor R = Se3Matrix.rotation(g);
    Tensor t = Se3Matrix.translation(g);
    Tensor u = u_w.extract(0, 3); // translation
    Tensor w = u_w.extract(3, 6); // rotation
    Tensor rw = R.dot(w);
    return Join.of( //
        R.dot(u).add(Cross.of(t, rw)), //
        rw);
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor Rt, Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int dimensions() {
    return 6;
  }
}
