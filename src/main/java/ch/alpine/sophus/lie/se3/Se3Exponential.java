// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.se.TSeMemberQ;
import ch.alpine.sophus.lie.so.So3Exponential;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.lie.rot.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.nrm.Vector2Norm;

/** from "Lie Groups for 2D and 3D Transformations" by Ethan Eade
 * http://ethaneade.com/ */
public enum Se3Exponential implements Exponential {
  INSTANCE;

  private static final Tensor ID3 = IdentityMatrix.of(3);

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    MatrixQ.requireSize(v, 4, 4);
    Tensor u = Drop.tail(v.get(Tensor.ALL, 3), 1); // translation
    Tensor w = So3Exponential.vectorize(v);
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
    Tensor wx = So3Exponential.INSTANCE.log(R);
    Tensor w = So3Exponential.vectorize(wx);
    Scalar theta = Vector2Norm.of(w);
    Tensor wx2 = wx.dot(wx);
    Se3Numerics se3Numerics = new Se3Numerics(theta);
    Tensor Vi = ID3.subtract(wx.multiply(Rational.HALF)).add(wx2.multiply(se3Numerics.D));
    Tensor t = Se3Matrix.translation(g);
    return Se3Matrix.ofT(wx, Vi.dot(t));
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return TSeMemberQ.INSTANCE;
  }
}
