// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrMetricTest extends TestCase {
  /** @param vector
   * @return matrix that projects points to line spanned by vector */
  private static Tensor projection1(Tensor vector) {
    return projection(Tensor.of(vector.stream().map(Tensors::of)));
  }

  /** @param design matrix
   * @return */
  private static Tensor projection(Tensor design) {
    return InfluenceMatrix.of(design).matrix();
  }

  public void testSimple() {
    Tensor log = MatrixLog.of(IdentityMatrix.of(3));
    assertEquals(log, Array.zeros(3, 3));
  }

  public void testLog() {
    Tensor log = MatrixLog.of(Tensors.fromString("{{1, 0.1}, {0.2, 1}}"));
    Chop._10.requireClose(log, //
        Tensors.fromString("{{-0.01010135365876013, 0.10067478275975056}, {0.20134956551950084, -0.01010135365875986}}"));
  }

  public void testDistance2d() {
    Tensor p = projection1(Tensors.vector(0.2, 0.5));
    Tensor q = projection1(Tensors.vector(0.3, -0.1));
    Scalar distance = GrMetric.INSTANCE.distance(p, q);
    Chop._10.requireClose(distance, RealScalar.of(2.138348187726219));
  }

  public void testDistance3d() {
    Tensor x = Tensors.vector(0.2, 0.5, 0.1);
    Tensor y = Tensors.vector(0.3, -0.1, 1.4);
    Tensor p = projection1(x);
    Tensor q = projection1(y);
    Scalar distance = GrMetric.INSTANCE.distance(p, q);
    Chop._10.requireClose(distance, RealScalar.of(1.9499331103710236));
  }
}
