// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.lie.rot.Cross;
import ch.alpine.tensor.pdf.RandomSampleInterface;

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
class Se3Group implements LieGroup, RandomSampleInterface, VectorEncodingMarker, Serializable {
  public static final Se3Group INSTANCE = new Se3Group();

  private Se3Group() {
    // ---
  }

  @Override
  public MemberQ isPointQ() {
    return VectorQ.ofLength(dimensions());
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Exponential exponential0() {
    return Se3Exponential0.INSTANCE;
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

  @Override
  public Tensor neutral(Tensor element) {
    VectorQ.requireLength(element, 6);
    return element.maps(Scalar::zero);
  }

  @Override
  public Tensor combine(Tensor element1, Tensor element2) {
    return null;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return null;
  }
}
