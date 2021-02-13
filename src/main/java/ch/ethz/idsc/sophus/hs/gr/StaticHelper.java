// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorWedge;

/* package */ enum StaticHelper {
  ;
  /** Reference: geomstats - grassmannian.py
   * 
   * @param v square matrix
   * @return v projected to tangent space at x */
  public static Tensor project(Tensor x, Tensor v) {
    return MatrixBracket.of(x, TensorWedge.of(v));
    // FIXME
    // v = Symmetrize.of(v);
    // return x.dot(v).add(v.dot(x));
  }
  /** @param v
   * @return antisymmetric part of v is projected to tangent space */
  // public Tensor projectAntisym(Tensor x, Tensor v) {
  // v = TensorWedge.of(v);
  // return x.dot(v).subtract(v.dot(x)); // matrix bracket
  // }
}
