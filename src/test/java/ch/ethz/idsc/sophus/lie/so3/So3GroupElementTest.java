// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class So3GroupElementTest extends TestCase {
  private static final LieExponential LIE_EXPONENTIAL = So3Exponential.INSTANCE;
  private static final LieGroup LIE_GROUP = So3Group.INSTANCE;

  public void testBlub() {
    Tensor orth = LIE_EXPONENTIAL.exp(Tensors.vector(-.2, .3, .1));
    Tensor matr = LIE_EXPONENTIAL.exp(Tensors.vector(+.1, .2, .3));
    So3GroupElement.of(orth).combine(matr);
    try {
      So3GroupElement.of(orth).combine(matr.add(matr));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testAdjoint() {
    Tensor orth = So3Exponential.INSTANCE.exp(Tensors.vector(-.2, .3, .1));
    So3GroupElement so3GroupElement = So3GroupElement.of(orth);
    Tensor vector = Tensors.vector(1, 2, 3);
    Tensor adjoint = so3GroupElement.adjoint(vector);
    Chop._12.requireClose(orth.dot(vector), adjoint);
  }

  public void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = LIE_EXPONENTIAL.exp(RandomVariate.of(distribution, 3)); // element
      Tensor x = RandomVariate.of(distribution, 3); // vector
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = ge.combine(LIE_EXPONENTIAL.exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.element(LIE_EXPONENTIAL.exp(ge.adjoint(x))).combine(g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = LIE_EXPONENTIAL.exp(RandomVariate.of(distribution, 3));
      Tensor m = LIE_EXPONENTIAL.exp(RandomVariate.of(distribution, 3));
      LieGroupElement ge = LIE_GROUP.element(g);
      Tensor lhs = LIE_EXPONENTIAL.log( //
          LIE_GROUP.element(ge.combine(m)).combine(ge.inverse().toCoordinate())); // Log[g.m.g^-1]
      Tensor rhs = ge.adjoint(LIE_EXPONENTIAL.log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  public void testSimple() {
    So3GroupElement so3GroupElement = So3GroupElement.of(IdentityMatrix.of(3));
    so3GroupElement.inverse();
    try {
      so3GroupElement.combine(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail() {
    try {
      So3GroupElement.of(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testSizeFail() {
    try {
      So3GroupElement.of(IdentityMatrix.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
