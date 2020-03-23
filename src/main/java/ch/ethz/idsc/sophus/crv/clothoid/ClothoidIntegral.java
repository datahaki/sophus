// code by ureif
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;

public interface ClothoidIntegral {
  /** @param t
   * @return approximate integration of exp i*clothoidQuadratic on [0, t] */
  Scalar il(Scalar t);

  /** @param t
   * @return approximate integration of exp i*clothoidQuadratic on [t, 1] */
  Scalar ir(Scalar t);

  /** @return approximate integration of exp i*clothoidQuadratic on [0, 1] */
  Scalar one();
}
