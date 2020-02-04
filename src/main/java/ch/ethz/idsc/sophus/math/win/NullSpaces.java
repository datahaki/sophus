// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.lie.QRDecomposition;

public enum NullSpaces {
  ;
  // TODO improve, then bring to tensor lib
  public static Tensor of(Tensor matrix) {
    int d = Unprotect.dimension1(matrix);
    QRDecomposition qrDecomposition = QRDecomposition.of(matrix);
    return Tensor.of(qrDecomposition.getInverseQ().stream().skip(d));
  }
}
