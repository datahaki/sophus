// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Frobenius;
import junit.framework.TestCase;

public class GrExponentialTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.fromString("{{0, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    Tensor v = Tensors.fromString("{{0, 0.2}, {-0.2, 0}}");
    Tensor exp = grExponential.exp(v);
    assertTrue(GrassmannQ.of(exp));
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
  }

  public void testPoint() {
    Tensor x = Tensors.fromString("{{1, 0}, {0, 1}}");
    GrExponential grExponential = new GrExponential(x);
    Tensor v = Tensors.fromString("{{0, 0.2}, {-0.2, 0}}");
    Tensor exp = grExponential.exp(v);
    GrassmannQ.require(exp);
    Tolerance.CHOP.requireClose(x, exp);
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireAllZero(log);
    Tensor vectorLog = grExponential.vectorLog(exp);
    Tolerance.CHOP.requireClose(vectorLog, Tensors.vector(0));
  }

  public void testShift() {
    Tensor x = StaticHelper.projection(Tensors.vector(0.2, 0.5));
    GrExponential grExponential = new GrExponential(x);
    Tensor v = Tensors.fromString("{{0, 0.2}, {-0.2, 0}}");
    Tensor exp = grExponential.exp(v);
    assertTrue(GrassmannQ.of(exp));
    Tensor log = grExponential.log(exp);
    Tolerance.CHOP.requireClose(v, log);
    Tensor vectorLog = grExponential.vectorLog(exp);
    Tolerance.CHOP.requireClose(vectorLog, Tensors.vector(-0.2));
  }

  public void testDesign() {
    Distribution distribution = UniformDistribution.unit();
    int n = 6;
    TangentSpace tangentSpace = RnManifold.INSTANCE.logAt(RandomVariate.of(distribution, 2));
    HsInfluence hsInfluence = new HsInfluence(tangentSpace, RandomVariate.of(distribution, n, 2));
    Tensor x = hsInfluence.matrix();
    GrassmannQ.require(x);
    GrExponential grExponential = new GrExponential(x);
    Tensor vpre = RandomVariate.of(distribution, n, n);
    Tensor v = vpre.subtract(Transpose.of(vpre));
    Tensor exp = grExponential.exp(v);
    GrassmannQ.require(exp);
    assertTrue(Scalars.lessThan(RealScalar.of(0.001), Frobenius.between(x, exp)));
  }
}
