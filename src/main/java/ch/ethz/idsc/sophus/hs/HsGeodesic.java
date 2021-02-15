// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

/** general implementation of geodesic using exp/log */
public class HsGeodesic implements GeodesicInterface, Serializable {
  private final HsManifold hsManifold;

  /** @param hsManifold */
  public HsGeodesic(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
  }

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Exponential exponential = hsManifold.exponential(p);
    Tensor log = exponential.log(q);
    return scalar -> exponential.exp(log.multiply(scalar));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
