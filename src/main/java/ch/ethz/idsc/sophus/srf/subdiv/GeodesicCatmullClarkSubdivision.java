// code by jph
package ch.ethz.idsc.sophus.srf.subdiv;

import java.util.Objects;

import ch.ethz.idsc.sophus.crv.subdiv.BSpline3CurveSubdivision;
import ch.ethz.idsc.sophus.crv.subdiv.CurveSubdivision;
import ch.ethz.idsc.sophus.math.SplitInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Array;

/** Reference:
 * "Recursively generated B-spline surfaces on arbitrary topological meshes"
 * by Catmull, Clark; Computer-Aided Design 16(6), 1978 */
public class GeodesicCatmullClarkSubdivision {
  private final SplitInterface splitInterface;
  private final CurveSubdivision curveSubdivision;

  public GeodesicCatmullClarkSubdivision(SplitInterface splitInterface) {
    this.splitInterface = Objects.requireNonNull(splitInterface);
    curveSubdivision = new BSpline3CurveSubdivision(splitInterface);
  }

  public Tensor quad(Tensor a1, Tensor a2, Tensor b1, Tensor b2) {
    Tensor c1 = splitInterface.midpoint(a1, a2);
    Tensor c2 = splitInterface.midpoint(b1, b2);
    return splitInterface.midpoint(c1, c2);
  }

  public Tensor refine(Tensor grid) {
    int rows = grid.length();
    int cols = Unprotect.dimension1(grid);
    int outr = 2 * rows - 1;
    int outc = 2 * cols - 1;
    Tensor array = Array.zeros(outr, outc);
    /** assign old points */
    for (int pix = 0; pix < rows; ++pix)
      for (int piy = 0; piy < cols; ++piy)
        array.set(grid.get(pix, piy), 2 * pix, 2 * piy);
    /** assign midpoints */
    for (int pix = 1; pix < rows; ++pix)
      for (int piy = 1; piy < cols; ++piy) {
        Tensor mid = quad( //
            grid.get(pix - 1, piy - 1), //
            grid.get(pix + 0, piy + 0), //
            grid.get(pix - 1, piy + 0), //
            grid.get(pix + 0, piy - 1));
        array.set(mid, 2 * pix - 1, 2 * piy - 1);
      }
    /** assign edges top to bottom */
    for (int pix = 2; pix < outr - 1; pix += 2)
      for (int piy = 1; piy < outc; piy += 2) {
        Tensor mid = quad( //
            array.get(pix + 0, piy - 1), //
            array.get(pix + 0, piy + 1), //
            array.get(pix - 1, piy + 0), //
            array.get(pix + 1, piy + 0));
        array.set(mid, pix, piy);
      }
    /** assign edges left to right */
    for (int pix = 1; pix < outr; pix += 2)
      for (int piy = 2; piy < outc - 1; piy += 2) {
        Tensor mid = quad( //
            array.get(pix - 1, piy + 0), //
            array.get(pix + 1, piy + 0), //
            array.get(pix + 0, piy - 1), //
            array.get(pix + 0, piy + 1));
        array.set(mid, pix, piy);
      }
    /** reposition center points */
    for (int pix = 2; pix < outr - 1; pix += 2)
      for (int piy = 2; piy < outc - 1; piy += 2) {
        Tensor mds = quad( //
            array.get(pix - 1, piy - 1), //
            array.get(pix + 1, piy + 1), //
            array.get(pix + 1, piy - 1), //
            array.get(pix - 1, piy + 1));
        Tensor eds = quad( //
            array.get(pix - 1, piy + 0), //
            array.get(pix + 1, piy + 0), //
            array.get(pix + 0, piy - 1), //
            array.get(pix + 0, piy + 1));
        Tensor cen = array.get(pix, piy);
        Tensor mid = splitInterface.split(mds, //
            splitInterface.split(eds, cen, RationalScalar.of(1, 5)), //
            RationalScalar.of(5, 4));
        array.set(mid, pix, piy);
      }
    /** assign border top bottom */
    array.set(curveSubdivision.string(grid.get(0)), 0);
    array.set(curveSubdivision.string(grid.get(rows - 1)), outr - 1);
    /** assign border left right */
    array.set(curveSubdivision.string(grid.get(Tensor.ALL, 0)), Tensor.ALL, 0);
    array.set(curveSubdivision.string(grid.get(Tensor.ALL, cols - 1)), Tensor.ALL, outc - 1);
    return array;
  }
}
