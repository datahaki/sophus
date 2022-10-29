// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.VectorAngle;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Sin;

/** real projective plane
 * 
 * Reference:
 * "Eichfeldtheorie" by Helga Baum, 2005, p. 22 */
public enum RpnManifold implements HomogeneousSpace, MetricManifold {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }

  @Override
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS ALG duplicate with SnManifold, possibly share baseclass with SnManifold extend
    Scalar a = SnManifold.INSTANCE.distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return scalar -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw new Throw(p, q); // when p == -q
    return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.argmax(this, chop);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    Scalar d_xy = VectorAngle.of(x, y).orElseThrow();
    return Min.of(d_xy, Pi.VALUE.subtract(d_xy));
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor v) {
    return Vector2Norm.of(v);
  }
}
