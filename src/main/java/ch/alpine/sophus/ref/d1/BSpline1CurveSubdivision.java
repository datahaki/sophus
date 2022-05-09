// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;

/** linear B-spline
 * 
 * the scheme interpolates the control points
 * 
 * Dyn/Sharon 2014 p.14 show that the contractivity factor is mu = 1/2 */
public class BSpline1CurveSubdivision extends AbstractBSpline1CurveSubdivision implements Serializable {
  private final GeodesicSpace geodesicSpace;

  /** @param geodesicSpace non-null
   * @throws Exception if given midpointInterface is null */
  public BSpline1CurveSubdivision(GeodesicSpace geodesicSpace) {
    this.geodesicSpace = Objects.requireNonNull(geodesicSpace);
  }

  @Override // from AbstractBSpline1CurveSubdivision
  public final Tensor midpoint(Tensor p, Tensor q) {
    return geodesicSpace.midpoint(p, q);
  }
}
