// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class Vectorize0Norm2Test extends TestCase {
  public void testSimple() {
    Tensor matrix = Symmetrize.of(RandomVariate.of(UniformDistribution.unit(), 3, 3));
    Scalar n1 = FrobeniusNorm.of(matrix);
    Tensor vector = Vectorize.of(matrix, 0);
    Scalar n2 = Vectorize0Norm2.INSTANCE.apply(vector);
    Tolerance.CHOP.requireClose(n1, n2);
  }

  public void testLengthFail() {
    AssertFail.of(() -> Vectorize0Norm2.INSTANCE.apply(Pi.VALUE));
    AssertFail.of(() -> Vectorize0Norm2.INSTANCE.apply(Tensors.vector()));
    AssertFail.of(() -> Vectorize0Norm2.INSTANCE.apply(Tensors.vector(1, 2)));
    AssertFail.of(() -> Vectorize0Norm2.INSTANCE.apply(Tensors.vector(1, 2, 3, 4)));
  }
}
