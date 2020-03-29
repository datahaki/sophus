// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

public class HsGeodesic implements GeodesicInterface, Serializable {
  private final HsExponential hsExponential;

  /** @param hsExponential */
  public HsGeodesic(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
  }

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Exponential exponential = hsExponential.exponential(p);
    Tensor log = exponential.log(q);
    return scalar -> exponential.exp(log.multiply(scalar));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
