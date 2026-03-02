// code by jph
package ch.alpine.sophus.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.SophusExperimental;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Outer;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.io.Pretty;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.MatrixRank;
import ch.alpine.tensor.num.PlausibleRational;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;

class MetricManifoldTest {
  static List<MetricManifold> metricManifolds() {
    return SophusExperimental.filter(MetricManifold.class);
  }

  @ParameterizedTest
  @MethodSource("metricManifolds")
  void testNon(MetricManifold metricManifold) {
    assumeTrue(metricManifold instanceof RandomSampleInterface);
    RandomSampleInterface rsi = (RandomSampleInterface) metricManifold;
    Tensor p = RandomSample.of(rsi);
    BilinearForm bilinearForm = metricManifold.bilinearForm(p);
    assertNotNull(bilinearForm);
    TangentSpace tangentSpace = metricManifold.tangentSpace(p);
    Tensor v = tangentSpace.log(p);
    LinearSubspace linearSubspace = LinearSubspace.of(tangentSpace.isTangentQ()::defect, Dimensions.of(v));
    Tensor V = linearSubspace.basis();
    Tensor B = Outer.of(bilinearForm::formEval, V, V);
    // IO.println(metricManifold);
    ScalarUnaryOperator suo = PlausibleRational.of(100);
    Tensor maps = B.maps(suo);
    Pretty.of(maps);
    // IO.println();
    SymmetricMatrixQ.INSTANCE.require(B);
    Scalar det = Det.of(B);
    assertFalse(Tolerance.CHOP.isZero(det));
    // ---
    Tensor P = tangentSpace.exp().slash(V.multiply(RealScalar.of(0.02)));
    Tensor R = tangentSpace.vectorLog().slash(P);
    // IO.println(metricManifold + " " + Dimensions.of(V) + " - " + Dimensions.of(R));
    // IO.println(tangentSpace);
    // IO.println(Pretty.of(R.maps(Round._3)));
    int rank = MatrixRank.of(R);
    assertEquals(rank, linearSubspace.dimensions());
  }
}
