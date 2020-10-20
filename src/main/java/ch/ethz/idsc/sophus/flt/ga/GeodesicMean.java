// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.tensor.itp.BinaryAverage;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.win.DirichletWindow;

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
