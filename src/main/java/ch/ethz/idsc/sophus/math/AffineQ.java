// code by ob, jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;

public enum AffineQ {
  ;
  /** @param vector
   * @param chop
   * @return given vector
   * @throws Exception if scalar entries of given mask do not add up to one */
  public static Tensor require(Tensor vector, Chop chop) {
    chop.requireClose(Total.of(vector), RealScalar.ONE);
    return vector;
  }

  /** @param vector
   * @return given vector
   * @throws Exception if scalar entries of given mask do not add up to one */
  public static Tensor require(Tensor vector) {
    return require(vector, Tolerance.CHOP);
  }

  /** @param vector
   * @param chop
   * @return given vector
   * @throws Exception if scalar entries of given vector do not add up to one
   * @throws Exception if either scalar entry in given vector is negative */
  public static Tensor requirePositiveOrZero(Tensor vector, Chop chop) {
    Scalar sum = vector.stream() //
        .map(Scalar.class::cast) //
        .map(Sign::requirePositiveOrZero) //
        .reduce(Scalar::add).get();
    chop.requireClose(sum, RealScalar.ONE);
    return vector;
  }
}
