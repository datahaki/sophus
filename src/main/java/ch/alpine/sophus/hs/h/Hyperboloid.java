// code by jph
package ch.alpine.sophus.hs.h;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.sca.Chop;

/** hyperboloid model with fast midpoint computation
 * 
 * Reference:
 * "Metric Spaces of Non-Positive Curvature"
 * by Martin R. Bridson, Andre Haefliger, 1999 */
public class Hyperboloid implements HomogeneousSpace, VectorEncodingMarker, MetricManifold, Serializable {
  public static final Hyperboloid INSTANCE = new Hyperboloid();

  // ---
  private Hyperboloid() {
  }

  /** for instance the point {0, 0, ..., 0} with n entries is member of H^n */
  @Override // from MemberQ
  public boolean isMember(Tensor p) {
    return VectorQ.of(p);
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return IterativeBiinvariantMean.reduce(this, Chop._12);
  }

  @Override // from Manifold
  public Exponential exponential(Tensor p) {
    return new HExponential(p);
  }

  @Override // from HomogeneousSpace
  public HsTransport hsTransport() {
    return new PoleLadder(this);
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return HBilinearForm.of(p);
  }

  @Override
  public int dimensions() {
    throw new Throw();
  }

  @Override
  public String toString() {
    return "H";
  }
}
