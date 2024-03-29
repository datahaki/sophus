// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Gilles Deslauriers and Serge Dubuc: Symmetric iterative interpolation processes
 * 
 * <pre>
 * weights = {3, -25, 150, 150, -25, 3} / 256
 * {(1 - a) (1 - b), a (1 - b), b, b , a (1 - b), (1 - b) (1 - a)} / 2
 * Solve[Thread[% == weights]]
 * </pre>
 * 
 * Another reference by Johannes Wallner:
 * "On convergent interpolatory subdivision schemes in Riemannian Geometry", p.2 */
public class SixPointCurveSubdivision extends AbstractSixPointCurveSubdivision {
  private static final Scalar PQ = RationalScalar.of(25, 22);
  private static final Scalar _R = RationalScalar.of(75, 64);

  /** @param geodesicSpace */
  public SixPointCurveSubdivision(GeodesicSpace geodesicSpace) {
    super(geodesicSpace);
  }

  @Override // from AbstractSixPointCurveSubdivision
  protected Tensor center(Tensor p, Tensor q, Tensor r, Tensor s, Tensor t, Tensor u) {
    Tensor pq = geodesicSpace.split(p, q, PQ);
    Tensor _r = geodesicSpace.split(pq, r, _R);
    Tensor ut = geodesicSpace.split(u, t, PQ);
    Tensor _s = geodesicSpace.split(ut, s, _R);
    return midpoint(_r, _s);
  }
}
