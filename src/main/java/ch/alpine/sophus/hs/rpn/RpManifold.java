// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.hs.s.SnManifold;
import ch.alpine.sophus.hs.s.UnitVectorQ;
import ch.alpine.sophus.math.FrobeniusForm;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.sophus.math.api.MetricManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.MemberQ;
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
public class RpManifold implements HomogeneousSpace, MetricManifold {
  public static final RpManifold INSTANCE = new RpManifold();

  @Override
  public final BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.argmax(this, Chop._10);
  }

  @Override // from Manifold
  public final Exponential exponential(Tensor point) {
    return new RpnExponential(point);
  }

  @Override
  public final HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override
  public final ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS ALG duplicate with SnManifold, possibly share baseclass with SnManifold extend
    Scalar a = SnManifold.INSTANCE.distance(p, q);
    if (Scalars.isZero(a)) // when p == q
      return _ -> p.copy();
    if (Tolerance.CHOP.isClose(a, Pi.VALUE))
      throw new Throw(p, q); // when p == -q
    return scalar -> Vector2Norm.NORMALIZE.apply(Tensors.of( //
        Sin.FUNCTION.apply(a.multiply(RealScalar.ONE.subtract(scalar))), //
        Sin.FUNCTION.apply(a.multiply(scalar))).dot(Tensors.of(p, q)));
  }

  @Override
  public final Scalar distance(Tensor x, Tensor y) {
    Scalar d_xy = VectorAngle.of(x, y).orElseThrow();
    return Min.of(d_xy, Pi.VALUE.subtract(d_xy));
  }

  @Override
  public MemberQ isPointQ() {
    return UnitVectorQ.INSTANCE;
  }

  @Override
  public final BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public String toString() {
    return "Rp";
  }
}
