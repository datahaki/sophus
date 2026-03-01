// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.LieExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.pi.LinearSubspace;

public enum LieAlgebraMatrixBasis {
  ;
  public static Tensor of(SpecificLieGroup specificGroup) {
    int n = specificGroup.matrixOrder();
    LieExponential lieExponential = specificGroup.exponential0();
    ZeroDefectArrayQ zeroDefectArrayQ = lieExponential.isTangentQ();
    LinearSubspace linearSubspace = LinearSubspace.of(zeroDefectArrayQ::defect, n, n);
    return linearSubspace.basis();
  }
}
