// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2.Se2RandomSample;
import ch.alpine.sophus.lie.se3.Se3Group;
import ch.alpine.sophus.lie.se3.Se3Matrix;
import ch.alpine.sophus.lie.so.Rodrigues;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class LieDifferencesTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 4);
    TensorUnaryOperator lieDifferences = //
        Serialization.copy(LieDifferences.of(RGroup.INSTANCE));
    assertEquals(lieDifferences.apply(tensor), Differences.of(tensor));
  }

  @Test
  void testSe3() {
    Distribution distribution = NormalDistribution.of(0, .1);
    Tensor tensor = Tensors.empty();
    for (int index = 0; index < 10; ++index)
      tensor.append(Se3Matrix.of( //
          Rodrigues.vectorExp(RandomVariate.of(distribution, 3)), RandomVariate.of(distribution, 3)));
    TensorUnaryOperator lieDifferences = LieDifferences.of(Se3Group.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 6));
  }

  @Test
  void testSe2Simple() throws ClassNotFoundException, IOException {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    TensorUnaryOperator lieDifferences = LieDifferences.of(Se2Group.INSTANCE);
    Tensor tensor = Serialization.copy(lieDifferences).apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, 0));
  }

  @Test
  void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomSample.of(Se2RandomSample.of(distribution), 10);
    TensorUnaryOperator lieDifferences = LieDifferences.of(Se2Group.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 3));
  }

  @Test
  void testSe3Simple() {
    Tensor m1 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(0.2, -0.3, 0.4)), Tensors.vector(10, 20, 30));
    Tensor m2 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.4)), Tensors.vector(11, 21, 31));
    TensorUnaryOperator lieDifferences = LieDifferences.of(Se3Group.INSTANCE);
    Tensor tensor = lieDifferences.apply(Tensors.of(m1, m2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 6));
  }

  @Test
  void testHeSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    int d = 3;
    Tensor elements = RandomVariate.of(distribution, n, d * 2 + 1);
    TensorUnaryOperator lieDifferences = LieDifferences.of(HeGroup.INSTANCE);
    Tensor differences = Serialization.copy(lieDifferences).apply(elements);
    assertEquals(differences.length(), n - 1);
  }

  @Test
  void testSe2Covering() {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    TensorUnaryOperator INSTANCE = LieDifferences.of(Se2CoveringGroup.INSTANCE);
    Tensor tensor = INSTANCE.apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, Math.PI * 2));
  }

  @Test
  void testLieGroupNullFail() {
    assertThrows(Exception.class, () -> LieDifferences.of(null));
  }
}
