// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.api.BilinearForm;
import ch.alpine.sophus.api.MetricManifold;
import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.s.SnManifold;
import ch.alpine.sophus.hs.s.SnTransport;
import ch.alpine.sophus.hs.s.UnitVectorQ;
import ch.alpine.sophus.math.FrobeniusForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.nrm.VectorAngle;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Chop;

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
  public final TangentSpace tangentSpace(Tensor point) {
    return new RpnTangentSpace(point);
  }

  @Override
  public final HsTransport hsTransport() {
    return SnTransport.INSTANCE;
  }

  @Override
  public final ScalarTensorFunction curve(Tensor p, Tensor q) {
    Scalar dp = SnManifold.INSTANCE.distance(p, q);
    Scalar dn = SnManifold.INSTANCE.distance(p, q.negate());
    return SnManifold.INSTANCE.curve(p, Scalars.lessEquals(dp, dn) ? q : q.negate());
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
