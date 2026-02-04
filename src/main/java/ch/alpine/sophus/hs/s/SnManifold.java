// code by jph
package ch.alpine.sophus.hs.s;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.VectorAngle;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Sin;

/** The distance between two point on the d-dimensional sphere
 * embedded in R^(d+1) is the vector angle between the points.
 * 
 * SnMetric is equivalent to
 * <pre>
 * RnNorm.INSTANCE.norm(new SnExponential(p).log(q))
 * VectorAngle.of(p, q).get()
 * </pre>
 * 
 * @see SnAngle
 * @see VectorAngle */
public class SnManifold implements HomogeneousSpace, MetricManifold, Serializable {
  public static final SnManifold INSTANCE = new SnManifold();

  @Override // from MemberQ
  public boolean isMember(Tensor p) {
    return VectorQ.of(p) //
        && Tolerance.CHOP.isClose(Vector2Norm.of(p), RealScalar.ONE);
  }

  /** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
   * weights w_i == 1/N is sufficient for convergence to the mean.
   * 
   * Reference:
   * "Spherical averages and applications to spherical splines and interpolation"
   * by S. R. Buss, J. P. Fillmore, 2001 */
  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.of(this, Chop._10, SnPhongMean.INSTANCE);
  }

  @Override // from Manifold
  public Exponential exponential(Tensor point) {
    return new SnExponential(point);
  }

  @Override // from HomogeneousSpace
  public Tensor flip(Tensor p, Tensor q) {
    Tensor r = p.multiply((Scalar) p.dot(q));
    return r.add(r).subtract(q);
  }

  @Override // from HomogeneousSpace
  public Tensor midpoint(Tensor p, Tensor q) {
    return Vector2Norm.NORMALIZE.apply(p.add(q));
  }

  @Override // from HomogeneousSpace
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
    Scalar a = distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return _ -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw new Throw(p, q); // when p == -q
    return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public String toString() {
    return "S";
  }
}
