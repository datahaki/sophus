// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.r2.AngleVector;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnExponentialTest extends TestCase {
  public void test2D() throws ClassNotFoundException, IOException {
    SnExponential snExp = Serialization.copy(new SnExponential(UnitVector.of(2, 0)));
    Scalar dist = RealScalar.of(0.2);
    Tensor log = snExp.log(AngleVector.of(dist));
    Chop._12.requireClose(log, UnitVector.of(2, 1).multiply(dist));
  }

  public void test2DNormFail() {
    AssertFail.of(() -> new SnExponential(Tensors.vector(2, 1)));
  }

  public void test2DExpFail() {
    SnExponential snExp = new SnExponential(UnitVector.of(2, 0));
    Scalar dist = RealScalar.of(0.2);
    AssertFail.of(() -> snExp.exp(AngleVector.of(dist)));
  }

  public void test3D() {
    Tensor tensor = new SnExponential(UnitVector.of(3, 0)).exp(UnitVector.of(3, 1).multiply(RealScalar.of(Math.PI / 2)));
    Chop._12.requireClose(tensor, UnitVector.of(3, 1));
  }

  public void test4D() {
    Tensor tensor = new SnExponential(UnitVector.of(4, 0)).exp(UnitVector.of(4, 1).multiply(RealScalar.of(Math.PI)));
    Chop._12.requireClose(tensor, UnitVector.of(4, 0).negate());
  }

  public void testId() {
    for (int dim = 2; dim < 6; ++dim)
      for (int count = 0; count < 20; ++count) {
        Tensor point = Vector2Norm.NORMALIZE.apply(RandomVariate.of(NormalDistribution.standard(), dim));
        Tensor apply = new SnExponential(point).exp(point.map(Scalar::zero));
        Tolerance.CHOP.requireClose(point, apply);
      }
  }

  public void testLog() {
    Tensor point = UnitVector.of(3, 0);
    SnExponential snExp = new SnExponential(point);
    Tensor g = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1));
    Tensor vector = snExp.log(g);
    Tensor retr = snExp.exp(vector);
    Chop._10.requireClose(g, retr);
  }

  public void test0Fail() {
    AssertFail.of(() -> new SnExponential(Tensors.empty()));
  }

  public void testDim0Len1() {
    Tensor p = UnitVector.of(1, 0);
    SnExponential snExponential = new SnExponential(p);
    snExponential.exp(Array.zeros(1));
    Tensor v = snExponential.log(p);
    assertEquals(v, Array.zeros(1));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> new SnExponential(HilbertMatrix.of(3)));
  }

  public void testLogMemberFail() {
    SnExponential snExponential = new SnExponential(UnitVector.of(3, 0));
    AssertFail.of(() -> snExponential.log(Tensors.vector(1, 2, 3)));
  }
}
