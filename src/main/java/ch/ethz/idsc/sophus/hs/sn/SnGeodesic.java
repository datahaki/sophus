// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Sin;

/** geodesic on n-dimensional sphere embedded in R^(n+1)
 * 
 * implementation is based on the function "slerp" taken from
 * "Freeform Curves on Spheres of Arbitrary Dimension"
 * by Scott Schaefer and Ron Goldman, 2005, page 2
 * 
 * <p>the use of SnGeodesic.INSTANCE is preferred over HsGeodesic.
 * The implementation is symmetric in p and q and more efficient than HsGeodesic. */
public enum SnGeodesic implements GeodesicInterface {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override // from ParametricCurve
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Scalar a = SnMetric.INSTANCE.distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return scalar -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw TensorRuntimeException.of(p, q); // when p == -q
    return scalar -> NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  /** p and q are vectors of length 3 with unit length
   * 
   * Careful: function does not check length of input vectors! */
  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return NORMALIZE.apply(p.add(q));
  }
}
