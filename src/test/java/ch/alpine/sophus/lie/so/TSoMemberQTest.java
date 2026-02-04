package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomSample;

class TSoMemberQTest {
  @ParameterizedTest
  @ValueSource(ints = { 3, 4, 5 })
  void testAntisymm(int n) {
    TensorUnaryOperator tuo = AntisymmetricMatrixQ.INSTANCE::constraint;
    Tensor tensor = LinearSubspace.of(tuo, n, n).basis();
    assertEquals(tensor.length(), n * (n - 1) / 2);
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 4 })
  void testAntisymmAtPoint(int n) {
    SoNGroup soNGroup = new SoNGroup(n);
    Tensor p = RandomSample.of(soNGroup);
    TensorUnaryOperator tuo = v -> Transpose.of(v).dot(p).add(Transpose.of(p).dot(v));
    Tensor tensor = LinearSubspace.of(tuo, n, n).basis();
    assertEquals(tensor.length(), n * (n - 1) / 2);
  }

  @Test
  void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    TSoMemberQ.INSTANCE.requireMember(wedge);
    assertFalse(TSoMemberQ.INSTANCE.isMember(HilbertMatrix.of(3)));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> TSoMemberQ.INSTANCE.isMember(null));
  }
}
