// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.MatrixLog;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.sca.Chop;
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

  public void testRandomSymmetry() {
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(4, 3);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Scalar d1 = GrMetric.INSTANCE.distance(p, q);
    Scalar d2 = GrMetric.INSTANCE.distance(q, p);
    Tolerance.CHOP.requireClose(d1, d2);
  }

  public void testFrobenius() {
    RandomSampleInterface randomSampleInterface = GrRandomSample.of(4, 3);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Scalar d1 = GrMetric.INSTANCE.distance(p, q);
    Scalar d2 = FrobeniusNorm.of(new GrExponential(p).log(q));
    Tolerance.CHOP.requireClose(d1, d2);
  }

  public void testAntipodal() {
    Tensor p = DiagonalMatrix.of(1, 0);
    Tensor q = DiagonalMatrix.of(0, 1);
    GrMemberQ.INSTANCE.require(p);
    GrMemberQ.INSTANCE.require(q);
    Scalar d1 = GrMetric.INSTANCE.distance(p, q);
    d1.zero();
    Scalar d2 = LowerVectorize0_2Norm.INSTANCE.norm(new GrExponential(p).vectorLog(q));
    Tolerance.CHOP.requireClose(d1, d2);
    // TODO check distance of "antipodal" frames, why is this zero?
    // System.out.println(distance);
  }
}
