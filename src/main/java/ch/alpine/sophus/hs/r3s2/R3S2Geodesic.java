// code by jph
package ch.alpine.sophus.hs.r3s2;

import ch.alpine.sophus.hs.sn.SnRotationMatrix;
import ch.alpine.sophus.lie.se3.Se3Geodesic;
import ch.alpine.sophus.lie.se3.Se3Matrix;
import ch.alpine.sophus.math.Geodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.IdentityMatrix;

/** R3S2 represents the space of positions in R^3
 * and the sphere S^2 at each point.
 * An elements from the sphere represent a surface normal.
 * 
 * Elements of R3S2 are tensors of the form
 * {{px, py, pz}, {nx, ny, nz}} */
public enum R3S2Geodesic implements Geodesic {
  INSTANCE;

  private static final Tensor ID3 = IdentityMatrix.of(3);

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor pt = p.get(0);
    Tensor pn = p.get(1);
    Tensor qt = q.get(0);
    Tensor qn = q.get(1);
    Tensor rotation = SnRotationMatrix.of(pn, qn);
    Tensor pSe3 = Se3Matrix.of(ID3, pt);
    Tensor qSe3 = Se3Matrix.of(rotation, qt);
    return scalar -> {
      Tensor split = Se3Geodesic.INSTANCE.split(pSe3, qSe3, scalar);
      return Tensors.of( //
          Se3Matrix.translation(split), //
          Se3Matrix.rotation(split).dot(pn));
    };
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
