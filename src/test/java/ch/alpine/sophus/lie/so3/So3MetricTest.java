// code by jph
package ch.alpine.sophus.lie.so3;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class So3MetricTest {
  @Test
  public void testSimple() {
    Tensor vector = Tensors.vector(0.2, 0.5, 0.3);
    Scalar distance = So3Metric.INSTANCE.distance( //
        Rodrigues.vectorExp(Tensors.vector(0, 0, 0)), //
        Rodrigues.vectorExp(vector));
    Chop._15.requireClose(distance, Vector2Norm.of(vector));
  }

  @Test
  public void test4x4Fail() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor p = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    Tensor q = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    OrthogonalMatrixQ.require(p);
    AssertFail.of(() -> So3Metric.INSTANCE.distance(p, q));
  }
}
