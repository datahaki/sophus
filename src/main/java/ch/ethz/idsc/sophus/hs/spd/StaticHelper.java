// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sqrt;

/* package */ enum StaticHelper {
  ;
  /** n(g) == n(Inverse[g])
   * 
   * For the identity matrix, n(Id) == 0
   * 
   * @param g spd
   * @return */
  public static Scalar norm(Tensor g) {
    return Eigensystem.ofSymmetric(g).values().stream() //
        .map(Scalar.class::cast) //
        .map(Log.FUNCTION) //
        .map(AbsSquared.FUNCTION) //
        .reduce(Scalar::add) //
        .map(Sqrt.FUNCTION) // code style of gjoel
        .get();
  }
}
