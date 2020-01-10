// code by jph
package ch.ethz.idsc.sophus.flt;

import java.io.IOException;

import ch.ethz.idsc.sophus.flt.ga.BinomialWeights;
import ch.ethz.idsc.sophus.flt.ga.GeodesicCenter;
import ch.ethz.idsc.sophus.hs.sn.SnGeodesic;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.so3.So3Exponential;
import ch.ethz.idsc.sophus.lie.so3.So3Geodesic;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.win.HammingWindow;
import ch.ethz.idsc.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class CenterFilterTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    TensorUnaryOperator geodesicCenter = Serialization.copy(GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE));
    TensorUnaryOperator centerFilter = Serialization.copy(CenterFilter.of(geodesicCenter, 3));
    Tensor linear = Range.of(0, 10);
    Tensor result = centerFilter.apply(linear);
    assertEquals(result, linear);
    ExactTensorQ.require(result);
  }

  public void testKernel3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = CenterFilter.of(geodesicCenter, 3);
    Tensor signal = UnitVector.of(9, 4);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 0, 1/16, 15/64, 5/16, 15/64, 1/16, 0, 0}"));
  }

  public void testKernel1() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = CenterFilter.of(geodesicCenter, 1);
    Tensor signal = UnitVector.of(5, 2);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 1/4, 1/2, 1/4, 0}"));
  }

  public void testS2() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(SnGeodesic.INSTANCE, HannWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = CenterFilter.of(geodesicCenter, 1);
    Distribution distribution = NormalDistribution.standard();
    TensorUnaryOperator tensorUnaryOperator = Normalize.with(Norm._2);
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(tensorUnaryOperator));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  public void testSo3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(So3Geodesic.INSTANCE, HammingWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = CenterFilter.of(geodesicCenter, 1);
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(So3Exponential.INSTANCE::exp));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  public void testNegativeRadiusFail() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGeodesic.INSTANCE, BinomialWeights.INSTANCE);
    CenterFilter.of(geodesicCenter, 0);
    try {
      CenterFilter.of(geodesicCenter, -1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail() {
    try {
      CenterFilter.of(null, 1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
