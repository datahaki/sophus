// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Total;

public enum NormalizeAffine {
  ;
  public static Tensor of(Tensor target, Tensor a) {
    return of(target.dot(a));
  }

  public static Tensor of(Tensor target, Tensor a, Tensor b) {
    return of(target.dot(a).dot(b));
  }

  public static Tensor of(Tensor weights) {
    Scalar total = Total.ofVector(weights);
    return Tolerance.CHOP.allZero(total) //
        ? UnitVector.of(weights.length(), ArgMax.of(weights))
        : weights.divide(total);
  }
}
