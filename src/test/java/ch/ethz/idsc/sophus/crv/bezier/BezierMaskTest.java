// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.itp.BernsteinBasis;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.BinomialDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.PDF;
import ch.ethz.idsc.tensor.red.Total;
import junit.framework.TestCase;

public class BezierMaskTest extends TestCase {
  public void testSimple1() {
    assertEquals(BezierMaskTest.of(1).apply(RealScalar.of(0.2)), Tensors.vector(1));
  }

  public void testSimple2() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(BezierMaskTest.of(2));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/2, 1/2}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 1}"));
  }

  public void testSimple3() {
    ScalarTensorFunction scalarTensorFunction = BezierMaskTest.of(3);
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/4, 1/2, 1/4}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 0, 1}"));
  }

  public void testDistribution() {
    for (int n = 5; n < 10; ++n)
      for (Scalar p : new Scalar[] { RationalScalar.of(1, 3), RationalScalar.of(6, 7) }) {
        Tensor vector = BezierMaskTest.of(n).apply(p);
        Distribution distribution = BinomialDistribution.of(n - 1, p);
        PDF pdf = PDF.of(distribution);
        Tensor cmp = Range.of(0, n).map(pdf::at);
        assertEquals(vector, cmp);
        ExactTensorQ.require(vector);
        ExactTensorQ.require(cmp);
      }
  }

  public void testFunctionMatch() {
    int n = 5;
    ScalarTensorFunction scalarTensorFunction = BezierFunction.of(RnGeodesic.INSTANCE, IdentityMatrix.of(n));
    Scalar p = RationalScalar.of(2, 7);
    Tensor vector = scalarTensorFunction.apply(p);
    Tensor weight = BezierMaskTest.of(n).apply(p);
    ExactTensorQ.require(weight);
    ExactTensorQ.require(vector);
    assertEquals(weight, vector);
  }

  public void testExtrapolate() {
    assertEquals(BezierMaskTest.extrapolate(2), Tensors.fromString("{-1, 2}"));
    assertEquals(BezierMaskTest.extrapolate(3), Tensors.fromString("{1/4, -3/2, 9/4}"));
    assertEquals(BezierMaskTest.extrapolate(4), Tensors.fromString("{-1/27, 4/9, -16/9, 64/27}"));
    for (int n = 2; n < 10; ++n) {
      Tensor mask = BezierMaskTest.extrapolate(n);
      assertEquals(Total.of(mask), RealScalar.ONE);
      ExactTensorQ.require(mask);
    }
  }

  public void testNegFail() {
    AssertFail.of(() -> BezierMaskTest.of(0));
    AssertFail.of(() -> BezierMaskTest.of(-1));
  }

  /** The weight mask is generated by the following formula
   * <pre>
   * With[{p = n / (n - 1)}, Table[Binomial[n - 1, k] (1 - p)^(n - k - 1) p^k, {k, 0, n - 1}]]
   * </pre>
   * 
   * The leading coefficient converges to
   * <pre>
   * Limit[(n/(n - 1))^(n - 1), n -> Infinity] == Exp[1]
   * </pre>
   * 
   * @param n
   * @return weight mask of length n with entries that sum up to 1 */
  public static Tensor extrapolate(int n) {
    int nm1 = Integers.requirePositive(n) - 1;
    Scalar p = RationalScalar.of(n, nm1);
    return BernsteinBasis.of(nm1, p);
  }

  /** @param n positive
   * @return vector of length n */
  public static ScalarTensorFunction of(int n) {
    int nm1 = Integers.requirePositive(n) - 1;
    return p -> BernsteinBasis.of(nm1, p);
  }
}
