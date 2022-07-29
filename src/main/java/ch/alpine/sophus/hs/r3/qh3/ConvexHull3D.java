// code by jph
package ch.alpine.sophus.hs.r3.qh3;

import ch.alpine.qhull3d.QuickHull3D;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.io.Primitives;

/** uses QuickHull3D by John E. Lloyd, Fall 2004 */
public enum ConvexHull3D {
  ;
  /** @param tensor
   * @return */
  public static int[][] of(Tensor tensor) {
    tensor.stream().forEach(vector -> VectorQ.requireLength(vector, 3));
    QuickHull3D quickHull3D = new QuickHull3D();
    quickHull3D.build(Primitives.toDoubleArray(tensor));
    return quickHull3D.getFaces(QuickHull3D.POINT_RELATIVE);
  }
}
