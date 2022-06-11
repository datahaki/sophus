// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Sin;

public enum SnManifold implements HomogeneousSpace {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor point) {
    return new SnExponential(point);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    Tensor r = p.multiply((Scalar) p.dot(q));
    return r.add(r).subtract(q);
  }

  @Override
  public Tensor midpoint(Tensor p, Tensor q) {
    return Vector2Norm.NORMALIZE.apply(p.add(q));
  }

  @Override
  public HsTransport hsTransport() {
    return SnTransport.INSTANCE;
  }

  /** geodesic on n-dimensional sphere embedded in R^(n+1)
   * 
   * implementation is based on the function "slerp" taken from
   * "Freeform Curves on Spheres of Arbitrary Dimension"
   * by Scott Schaefer and Ron Goldman, 2005, page 2
   * 
   * The implementation is symmetric in p and q and more efficient
   * than the default implementation. */
  @Override // from GeodesicSpace
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Scalar a = SnMetric.INSTANCE.distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return scalar -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw TensorRuntimeException.of(p, q); // when p == -q
    return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  /** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
   * weights w_i == 1/N is sufficient for convergence to the mean.
   * 
   * Reference:
   * "Spherical averages and applications to spherical splines and interpolation"
   * by S. R. Buss, J. P. Fillmore, 2001 */
  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.of(SnManifold.INSTANCE, chop, SnPhongMean.INSTANCE);
  }
}
