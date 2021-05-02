// code by jph
package ch.alpine.sophus.math;

import java.util.Arrays;
import java.util.Objects;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class NocopyTest extends TestCase {
  public void testArray() {
    Tensor[] tensors = new Tensor[5];
    Tensor tensor = Unprotect.using(Arrays.asList(tensors));
    tensors[0] = Tensors.vector(1, 2, 3, 4);
    tensors[2] = IdentityMatrix.of(3);
    assertEquals(tensor.get(0), Tensors.vector(1, 2, 3, 4));
    assertEquals(tensor.get(2), IdentityMatrix.of(3));
    int count = 0;
    for (Tensor s : tensor)
      count += Objects.isNull(s) ? 1 : 0;
    assertEquals(count, 3);
    AssertFail.of(() -> tensor.append(Tensors.vector(3)));
  }
}
