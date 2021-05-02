// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class Se3MatrixTest extends TestCase {
  public void testId() {
    Tensor tensor = Se3Matrix.of(IdentityMatrix.of(3), Array.zeros(3));
    assertEquals(tensor, IdentityMatrix.of(4));
  }

  public void testOfTranslation() {
    Tensor tensor = Se3Matrix.of(IdentityMatrix.of(3), Range.of(2, 5));
    Tensor row = tensor.get(Tensor.ALL, 3);
    assertEquals(row, Tensors.vector(2, 3, 4, 1));
  }

  public void testRotation() {
    Tensor matrix = Partition.of(Range.of(0, 16), 4);
    Tensor rotate = Se3Matrix.rotation(matrix);
    assertEquals(rotate, Tensors.fromString("{{0, 1, 2}, {4, 5, 6}, {8, 9, 10}}"));
  }

  public void testTranslation() {
    Tensor matrix = Partition.of(Range.of(0, 16), 4);
    Tensor vector = Se3Matrix.translation(matrix);
    assertEquals(vector, Tensors.vector(3, 7, 11));
  }
}
