// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.sca.win.DirichletWindow;

/** Careful: the implementation only supports sequences with odd number of elements
 * 
 * projects a sequence of points to their geodesic center
 * 
 * Example: if the points are from R^n the center would simply be the mean */
public enum GeodesicMean {
  ;
  /** @param binaryAverage
   * @return geodesic center operator with Dirichlet/constant weights */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage) {
    return GeodesicCenter.of(binaryAverage, DirichletWindow.FUNCTION);
  }
}
