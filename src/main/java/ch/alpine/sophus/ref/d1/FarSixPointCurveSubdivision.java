// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Important: the use of this variant of the six point scheme is not recommended
 * Instead, use {@link SixPointCurveSubdivision}
 * 
 * refinement according to
 * Dyn/Sharon 2014: Manifold-valued subdivision schemes based on geodesic inductive averaging
 * 
 * Dyn/Sharon 2014 p.20 show for this split a contractivity factor of mu = 0.9844
 * 
 * weights = {3, -25, 150, 150, -25, 3}/256;
 * {b (1 - a), 1 - b, a b, a b, 1 - b, b (1 - a)}/2
 * Solve[Thread[% == weights]] */
public class FarSixPointCurveSubdivision extends AbstractSixPointCurveSubdivision {
  private static final Scalar PR = RationalScalar.of(50, 51);
  private static final Scalar Q_ = RationalScalar.of(153, 128);

  /** @param geodesicSpace */
  public FarSixPointCurveSubdivision(GeodesicSpace geodesicSpace) {
    super(geodesicSpace);
  }

  @Override // from AbstractSixPointCurveSubdivision
  protected Tensor center(Tensor p, Tensor q, Tensor r, Tensor s, Tensor t, Tensor u) {
    Tensor pr = geodesicSpace.split(p, r, PR);
    Tensor q_ = geodesicSpace.split(q, pr, Q_);
    Tensor us = geodesicSpace.split(u, s, PR);
    Tensor t_ = geodesicSpace.split(t, us, Q_);
    return geodesicSpace.midpoint(q_, t_);
  }
}
