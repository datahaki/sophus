// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;

import ch.alpine.sophus.api.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.pi.LinearSubspace;

public class LieMatrixAlgebra implements Serializable {
  private final MatrixAlgebra matrixAlgebra;

  public LieMatrixAlgebra(SpecificLieGroup specificLieGroup) {
    int n = specificLieGroup.matrixOrder();
    LieExponential lieExponential = specificLieGroup.exponential0();
    ZeroDefectArrayQ zeroDefectArrayQ = lieExponential.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, n, n);
    matrixAlgebra = new MatrixAlgebra(linearSubspace.basis());
  }

  public MatrixAlgebra matrixAlgebra() {
    return matrixAlgebra;
  }

  public Tensor ad() {
    return matrixAlgebra.ad();
  }
}
