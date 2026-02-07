// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.pi.LinearSubspace;

class TSlMemberQTest {
  @Test
  void testSimple() {
    assertFalse(TSlMemberQ.INSTANCE.isMember(IdentityMatrix.of(2)));
    assertTrue(TSlMemberQ.INSTANCE.isMember(DiagonalMatrix.of(-1, 3, -2, 0)));
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4, 5, 6 })
  void testDims(int n) {
    LinearSubspace linearSubspace = LinearSubspace.of(TSlMemberQ.INSTANCE::defect, n, n);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(linearSubspace.basis());
    ExactTensorQ.require(linearSubspace.basis());
    SlNGroup seNGroup = new SlNGroup(n);
    assertEquals(linearSubspace.dimensions(), seNGroup.dimensions());
    ExactTensorQ.of(matrixAlgebra.ad());
  }
}
