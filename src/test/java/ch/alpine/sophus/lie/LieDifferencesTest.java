// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2.Se2RandomSample;
import ch.alpine.sophus.lie.se3.Se3Group;
import ch.alpine.sophus.lie.se3.Se3Matrix;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class LieDifferencesTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomVariate.of(distribution, 10, 4);
    LieDifferences lieDifferences = //
        Serialization.copy(new LieDifferences(RnGroup.INSTANCE));
    assertEquals(lieDifferences.apply(tensor), Differences.of(tensor));
  }

  @Test
  public void testPairFunction() {
    Distribution distribution = UniformDistribution.unit();
    RandomSampleInterface randomSampleInterface = Se2RandomSample.of(distribution);
    LieDifferences lieDifferences = new LieDifferences(Se2Group.INSTANCE);
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor v1 = lieDifferences.reduce(p, q);
      Tensor v2 = lieDifferences.reduce(q, p).negate();
      Chop._12.requireClose(v1, v2);
    }
  }

  @Test
  public void testSe3() {
    Distribution distribution = NormalDistribution.of(0, .1);
    Tensor tensor = Tensors.empty();
    for (int index = 0; index < 10; ++index)
      tensor.append(Se3Matrix.of( //
          Rodrigues.vectorExp(RandomVariate.of(distribution, 3)), RandomVariate.of(distribution, 3)));
    LieDifferences lieDifferences = new LieDifferences(Se3Group.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 2, 3));
  }

  @Test
  public void testLieGroupNullFail() {
    assertThrows(Exception.class, () -> new LieDifferences(null));
  }

  @Test
  public void testSe2Simple() throws ClassNotFoundException, IOException {
    Tensor p1 = Tensors.vector(0, 0, -Math.PI);
    Tensor p2 = Tensors.vector(0, 0, +Math.PI);
    LieDifferences lieDifferences = new LieDifferences(Se2Group.INSTANCE);
    Tensor tensor = Serialization.copy(lieDifferences).apply(Tensors.of(p1, p2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 3));
    Chop._14.requireClose(tensor.get(0), Tensors.vector(0, 0, 0));
  }

  @Test
  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = RandomSample.of(Se2RandomSample.of(distribution), 10);
    LieDifferences lieDifferences = new LieDifferences(Se2Group.INSTANCE);
    assertEquals(Dimensions.of(lieDifferences.apply(tensor)), Arrays.asList(9, 3));
  }

  @Test
  public void testSe3Simple() {
    Tensor m1 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(0.2, -0.3, 0.4)), Tensors.vector(10, 20, 30));
    Tensor m2 = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(-0.2, 0.3, 0.4)), Tensors.vector(11, 21, 31));
    LieDifferences lieDifferences = new LieDifferences(Se3Group.INSTANCE);
    Tensor tensor = lieDifferences.apply(Tensors.of(m1, m2));
    assertEquals(Dimensions.of(tensor), Arrays.asList(1, 2, 3));
  }

  @Test
  public void testHeSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    int d = 3;
    Tensor x = RandomVariate.of(distribution, n, d);
    Tensor y = RandomVariate.of(distribution, n, d);
    Tensor z = RandomVariate.of(distribution, n);
    Tensor elements = Tensor.of(IntStream.range(0, n).mapToObj(i -> Tensors.of(x.get(i), y.get(i), z.Get(i))));
    LieDifferences lieDifferences = new LieDifferences(HeGroup.INSTANCE);
    Tensor differences = Serialization.copy(lieDifferences).apply(elements);
    assertEquals(differences.length(), n - 1);
  }
}
