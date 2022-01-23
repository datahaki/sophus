// code by jph
package ch.alpine.sophus.flt;

import ch.alpine.sophus.flt.ga.BinomialWeights;
import ch.alpine.sophus.flt.ga.GeodesicCenter;
import ch.alpine.sophus.hs.sn.SnGeodesic;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.lie.so3.So3Geodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.win.HammingWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class CenterFilterTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator centerFilter = new CenterFilter(geodesicCenter, 3);
    Tensor linear = Range.of(0, 10);
    Tensor result = centerFilter.apply(linear);
    assertEquals(result, linear);
    ExactTensorQ.require(result);
  }

  public void testKernel3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 3);
    Tensor signal = UnitVector.of(9, 4);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 0, 1/16, 15/64, 5/16, 15/64, 1/16, 0, 0}"));
  }

  public void testKernel1() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Tensor signal = UnitVector.of(5, 2);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 1/4, 1/2, 1/4, 0}"));
  }

  public void testS2() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(SnGeodesic.INSTANCE, HannWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Distribution distribution = NormalDistribution.standard();
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(Vector2Norm.NORMALIZE));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  public void testSo3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(So3Geodesic.INSTANCE, HammingWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(Rodrigues::vectorExp));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  public void testNegativeRadiusFail() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    new CenterFilter(geodesicCenter, 0);
    AssertFail.of(() -> new CenterFilter(geodesicCenter, -1));
  }

  public void testFail() {
    AssertFail.of(() -> new CenterFilter(null, 1));
  }
}
