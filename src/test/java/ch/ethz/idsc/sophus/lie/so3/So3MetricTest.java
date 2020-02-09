// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3MetricTest extends TestCase {
  public void testSimple() {
    Tensor vector = Tensors.vector(0.2, 0.5, 0.3);
    Scalar distance = So3Metric.INSTANCE.distance( //
        So3Exponential.INSTANCE.exp(Tensors.vector(0, 0, 0)), //
        So3Exponential.INSTANCE.exp(vector));
    Chop._15.requireClose(distance, Norm._2.ofVector(vector));
  }

  public void test4x4Fail() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor p = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    Tensor q = MatrixExp.of(TensorWedge.of(RandomVariate.of(distribution, 4, 4)));
    OrthogonalMatrixQ.require(p);
    try {
      So3Metric.INSTANCE.distance(p, q);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
