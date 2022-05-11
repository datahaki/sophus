// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.hs.sn.SnMetric;
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

/** Reference:
 * "Eichfeldtheorie" by Helga Baum, 2005, p. 22 */
// TODO possibly share baseclass with SnManifold extend
public enum RpnManifold implements HomogeneousSpace {
  INSTANCE;

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }

  @Override
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override // from ParametricCurve
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS duplicate with SnManifold
    // TODO SOPHUS ALG
    Scalar a = SnMetric.INSTANCE.distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return scalar -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw TensorRuntimeException.of(p, q); // when p == -q
    return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.of(this, chop);
  }
}
