// code by jph
package ch.alpine.sophus.hs.r3.qh3;

import java.util.List;

import ch.alpine.qhull3d.QuickHull3D;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.qty.QuantityUnit;

/** uses QuickHull3D by John E. Lloyd, Fall 2004 */
public enum ConvexHull3D {
  ;
  /** @param tensor of size n x 3, representing a list of vertices in 3-dimensional Euclidean space
   * @return */
  public static List<int[]> of(Tensor tensor) {
    tensor.stream().forEach(vector -> VectorQ.requireLength(vector, 3));
    ScalarUnaryOperator suo = QuantityMagnitude.singleton(QuantityUnit.of(tensor.Get(0, 0)));
    QuickHull3D quickHull3D = new QuickHull3D(Primitives.toDoubleArray(tensor.map(suo)));
    quickHull3D.buildHull();
    return quickHull3D.getFaces();
  }
}
