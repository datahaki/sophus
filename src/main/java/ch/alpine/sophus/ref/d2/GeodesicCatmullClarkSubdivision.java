// code by jphrray
package ch.alpine.sophus.ref.d2;

import java.util.Arrays;
import java.util.Objects;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.ref.d1.BSpline3CurveSubdivision;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;

/** Reference:
 * "Recursively generated B-spline surfaces on arbitrary topological meshes"
 * by Catmull, Clark; Computer-Aided Design 16(6), 1978 */
public class GeodesicCatmullClarkSubdivision {
  private final GeodesicSpace geodesicSpace;
  private final CurveSubdivision curveSubdivision;

  public GeodesicCatmullClarkSubdivision(GeodesicSpace geodesicSpace) {
    this.geodesicSpace = Objects.requireNonNull(geodesicSpace);
    curveSubdivision = new BSpline3CurveSubdivision(geodesicSpace);
  }

  public Tensor quad(Tensor a1, Tensor a2, Tensor b1, Tensor b2) {
    return geodesicSpace.midpoint( //
        geodesicSpace.midpoint(a1, a2), //
        geodesicSpace.midpoint(b1, b2));
  }

  public Tensor refine(Tensor grid) {
    int rows = grid.length();
    int cols = Unprotect.dimension1(grid);
    int outr = 2 * rows - 1;
    int outc = 2 * cols - 1;
    Tensor[][] array = new Tensor[outr][outc];
    /** assign old points */
    for (int pix = 0; pix < rows; ++pix)
      for (int piy = 0; piy < cols; ++piy)
        array[2 * pix][2 * piy] = grid.get(pix, piy);
    /** assign midpoints */
    for (int pix = 1; pix < rows; ++pix)
      for (int piy = 1; piy < cols; ++piy)
        array[2 * pix - 1][2 * piy - 1] = quad( //
            grid.get(pix - 1, piy - 1), //
            grid.get(pix + 0, piy + 0), //
            grid.get(pix - 1, piy + 0), //
            grid.get(pix + 0, piy - 1));
    /** assign edges top to bottom */
    for (int pix = 2; pix < outr - 1; pix += 2)
      for (int piy = 1; piy < outc; piy += 2)
        array[pix][piy] = quad( //
            array[pix + 0][piy - 1], //
            array[pix + 0][piy + 1], //
            array[pix - 1][piy + 0], //
            array[pix + 1][piy + 0]);
    /** assign edges left to right */
    for (int pix = 1; pix < outr; pix += 2)
      for (int piy = 2; piy < outc - 1; piy += 2)
        array[pix][piy] = quad( //
            array[pix - 1][piy + 0], //
            array[pix + 1][piy + 0], //
            array[pix + 0][piy - 1], //
            array[pix + 0][piy + 1]);
    /** reposition center points */
    for (int pix = 2; pix < outr - 1; pix += 2)
      for (int piy = 2; piy < outc - 1; piy += 2) {
        Tensor mds = quad( //
            array[pix - 1][piy - 1], //
            array[pix + 1][piy + 1], //
            array[pix + 1][piy - 1], //
            array[pix - 1][piy + 1]);
        Tensor eds = quad( //
            array[pix - 1][piy + 0], //
            array[pix + 1][piy + 0], //
            array[pix + 0][piy - 1], //
            array[pix + 0][piy + 1]);
        Tensor cen = array[pix][piy];
        array[pix][piy] = geodesicSpace.split(mds, //
            geodesicSpace.split(eds, cen, RationalScalar.of(1, 5)), //
            RationalScalar.of(5, 4));
      }
    Tensor tensor = Tensor.of(Arrays.stream(array).map(Unprotect::byRef));
    /** assign border top bottom */
    tensor.set(curveSubdivision.string(grid.get(0)), 0);
    tensor.set(curveSubdivision.string(grid.get(rows - 1)), outr - 1);
    /** assign border left right */
    tensor.set(curveSubdivision.string(grid.get(Tensor.ALL, 0)), Tensor.ALL, 0);
    tensor.set(curveSubdivision.string(grid.get(Tensor.ALL, cols - 1)), Tensor.ALL, outc - 1);
    return tensor;
  }
}
