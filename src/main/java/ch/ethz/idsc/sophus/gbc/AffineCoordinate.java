// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** biinvariant generalized barycentric coordinates that do not satisfy the lagrange property */
public enum AffineCoordinate {
  ;
  private static final TensorUnaryOperator AFFINE = levers -> ConstantArray.of(RealScalar.ONE, levers.length());

  /** @param vectorLogManifold
   * @return biinvariant generalized barycentric coordinates */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold) {
    return AbsoluteCoordinate.custom(vectorLogManifold, AFFINE);
  }
}
