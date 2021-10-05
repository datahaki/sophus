// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.Geodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** general implementation of geodesic using exp/log */
public class HsGeodesic implements Geodesic, Serializable {
  private final HsManifold hsManifold;

  /** @param hsManifold */
  public HsGeodesic(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
  }

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Exponential exponential = hsManifold.exponential(p);
    Tensor log = exponential.log(q);
    return scalar -> exponential.exp(log.multiply(scalar));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return hsManifold.midpoint(p, q);
  }
}
