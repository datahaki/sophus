// code by jph
package ch.alpine.sophus.flt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.ga.BinomialWeights;
import ch.alpine.sophus.flt.ga.GeodesicCenter;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.lie.so3.So3Group;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.win.HammingWindow;
import ch.alpine.tensor.sca.win.HannWindow;

class CenterFilterTest {
  @Test
  void testSimple() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGroup.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator centerFilter = new CenterFilter(geodesicCenter, 3);
    Tensor linear = Range.of(0, 10);
    Tensor result = centerFilter.apply(linear);
    assertEquals(result, linear);
    ExactTensorQ.require(result);
  }

  @Test
  void testKernel3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGroup.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 3);
    Tensor signal = UnitVector.of(9, 4);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 0, 1/16, 15/64, 5/16, 15/64, 1/16, 0, 0}"));
  }

  @Test
  void testKernel1() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGroup.INSTANCE, BinomialWeights.INSTANCE);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Tensor signal = UnitVector.of(5, 2);
    Tensor result = geodesicCenterFilter.apply(signal);
    ExactTensorQ.require(result);
    assertEquals(result, Tensors.fromString("{0, 1/4, 1/2, 1/4, 0}"));
  }

  @Test
  void testS2() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(SnManifold.INSTANCE, HannWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Distribution distribution = NormalDistribution.standard();
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(Vector2Norm.NORMALIZE));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  @Test
  void testSo3() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(So3Group.INSTANCE, HammingWindow.FUNCTION);
    TensorUnaryOperator geodesicCenterFilter = new CenterFilter(geodesicCenter, 1);
    Distribution distribution = UniformDistribution.unit();
    Tensor tensor = Tensor.of(RandomVariate.of(distribution, 10, 3).stream().map(Rodrigues::vectorExp));
    Tensor result = geodesicCenterFilter.apply(tensor);
    assertEquals(Dimensions.of(tensor), Dimensions.of(result));
  }

  @Test
  void testNegativeRadiusFail() {
    TensorUnaryOperator geodesicCenter = GeodesicCenter.of(RnGroup.INSTANCE, BinomialWeights.INSTANCE);
    CenterFilter centerFilter = new CenterFilter(geodesicCenter, 0);
    assertEquals(centerFilter.radius(), 0);
    assertThrows(Exception.class, () -> new CenterFilter(geodesicCenter, -1));
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new CenterFilter(null, 1));
  }
}
