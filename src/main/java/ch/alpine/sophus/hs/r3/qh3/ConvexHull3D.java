// code by jph
package ch.alpine.sophus.hs.r3.qh3;

import java.util.List;

import ch.alpine.qhull3d.QuickHull3D;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.io.Primitives;

/** uses QuickHull3D by John E. Lloyd, Fall 2004 */
public enum ConvexHull3D {
  ;
  /** @param tensor of size n x 3
   * @return */
  public static List<int[]> of(Tensor tensor) {
    tensor.stream().forEach(vector -> VectorQ.requireLength(vector, 3));
    QuickHull3D quickHull3D = new QuickHull3D(Primitives.toDoubleArray(tensor));
    quickHull3D.buildHull();
    return quickHull3D.getFaces();
  }
}
