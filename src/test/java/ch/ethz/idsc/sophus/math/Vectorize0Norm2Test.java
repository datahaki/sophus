// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.FrobeniusNorm;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
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
