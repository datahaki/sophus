// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Ramp;

/** hyperboloid model with fast midpoint computation
 * 
 * Reference:
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnManifold implements HomogeneousSpace, MetricManifold {
  INSTANCE;
  // private final HsTransport hsTransport = ;

  @Override // from Manifold
  public Exponential exponential(Tensor p) {
    return new HnExponential(p);
  }

  @Override // from HomogeneousSpace
  public Tensor flip(Tensor p, Tensor q) {
    Scalar nxy = LBilinearForm.between(p, q).negate();
    return p.add(p).multiply(nxy).subtract(q);
  }

  @Override // from HomogeneousSpace
  public Tensor midpoint(Tensor p, Tensor q) {
    return HnProjection.INSTANCE.apply(p.add(q));
  }

  @Override // from HomogeneousSpace
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override // from HomogeneousSpace
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.of(this, chop, HnPhongMean.INSTANCE);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return new HnAngle(p).apply(q);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor v) {
    return HnHypot.of(v, Ramp.FUNCTION);
  }
}
