// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.KillingForm;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.spa.SparseArray;

class SlNGroupTest {
  @ParameterizedTest
  @ValueSource(ints = { 3, 4 })
  void testSl3(int n) {
    SlNGroup slNGroup = new SlNGroup(n);
    assertTrue(slNGroup.toString().contains("" + n));
    Tensor matrixBasis = MatrixAlgebra.of(slNGroup).basis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    assertEquals(matrixBasis.length(), matrixAlgebra.dimensions());
    Tensor ad = matrixAlgebra.ad();
    assertInstanceOf(SparseArray.class, ad);
    Tensor form = KillingForm.of(ad);
    // IO.println(Pretty.of(form));
    ExactTensorQ.require(form);
    assertInstanceOf(SparseArray.class, form);
    Scalar scalar = Det.of(form);
    ExactScalarQ.require(scalar);
  }
}
