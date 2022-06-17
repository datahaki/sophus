// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

/** hyperboloid model with fast midpoint computation
 * 
 * Reference:
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public enum HnManifold implements HomogeneousSpace, MetricManifold {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor p) {
    return new HnExponential(p);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    Scalar nxy = LBilinearForm.between(p, q).negate();
    return p.add(p).multiply(nxy).subtract(q);
  }

  @Override
  public Tensor midpoint(Tensor p, Tensor q) {
    return HnProjection.INSTANCE.apply(p.add(q));
  }

  @Override
  public HsTransport hsTransport() {
    return HnTransport.INSTANCE;
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.of(this, chop, HnPhongMean.INSTANCE);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return new HnAngle(p).apply(q);
  }

  @Override // from MetricManifold
  public Biinvariant biinvariant() {
    return HnMetricBiinvariant.INSTANCE;
  }
}
