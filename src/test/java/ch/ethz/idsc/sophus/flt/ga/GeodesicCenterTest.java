// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;

import ch.ethz.idsc.sophus.flt.ga.GeodesicCenter.Splits;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.lie.se2.Se2Geodesic;
import ch.ethz.idsc.sophus.math.win.UniformWindowSampler;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.win.DirichletWindow;
import ch.ethz.idsc.tensor.sca.win.GaussianWindow;
import ch.ethz.idsc.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class GeodesicCenterTest extends TestCase {
  @SuppressWarnings("unchecked")
  private static final Function<Integer, Tensor> CONSTANT = (Function<Integer, Tensor> & Serializable) //
  i -> Array.of(k -> RationalScalar.of(1, i), i);

  public void testSimple() {
    // function generates window to compute mean: all points in window have same weight
    TensorUnaryOperator tensorUnaryOperator = GeodesicCenter.of(RnGeodesic.INSTANCE, CONSTANT);
    for (int index = 0; index < 9; ++index) {
      Tensor apply = tensorUnaryOperator.apply(UnitVector.of(9, index));
      assertEquals(apply, RationalScalar.of(1, 9));
    }
  }

  public void testDirichlet() {
    // function generates window to compute mean: all points in window have same weight
    TensorUnaryOperator tensorUnaryOperator = GeodesicCenter.of(RnGeodesic.INSTANCE, DirichletWindow.FUNCTION);
    for (int index = 0; index < 9; ++index) {
      Tensor apply = tensorUnaryOperator.apply(UnitVector.of(9, index));
      assertEquals(apply, RationalScalar.of(1, 9));
    }
  }

  public void testSe2() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicCenter.of(Se2Geodesic.INSTANCE, smoothingKernel.get());
      Distribution distribution = UniformDistribution.unit();
      Tensor sequence = RandomVariate.of(distribution, 7, 3);
      Tensor tensor = tensorUnaryOperator.apply(sequence);
      assertEquals(Dimensions.of(tensor), Arrays.asList(3));
    }
  }

  public void testEvenFail() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicCenter.of(RnGeodesic.INSTANCE, CONSTANT);
    for (int index = 0; index < 9; ++index) {
      int fi = index;
      AssertFail.of(() -> tensorUnaryOperator.apply(Array.zeros(2 * fi)));
    }
  }

  public void testFail() {
    AssertFail.of(() -> GeodesicCenter.of(RnGeodesic.INSTANCE, (UniformWindowSampler) null));
    AssertFail.of(() -> GeodesicCenter.of(RnGeodesic.INSTANCE, (ScalarUnaryOperator) null));
    AssertFail.of(() -> GeodesicCenter.of(null, CONSTANT));
  }

  public void testSplitsMean() {
    Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(DirichletWindow.FUNCTION);
    {
      Tensor tensor = GeodesicCenter.Splits.of(uniformWindowSampler.apply(3));
      assertEquals(tensor, Tensors.fromString("{1/3}"));
    }
    {
      Tensor tensor = GeodesicCenter.Splits.of(uniformWindowSampler.apply(5));
      assertEquals(tensor, Tensors.fromString("{1/2, 1/5}"));
    }
    {
      Tensor tensor = GeodesicCenter.Splits.of(uniformWindowSampler.apply(7));
      assertEquals(tensor, Tensors.fromString("{1/2, 1/3, 1/7}"));
    }
  }

  public void testSplitsBinomial() {
    {
      Tensor tensor = GeodesicCenter.Splits.of(BinomialWeights.INSTANCE.apply(1 * 2 + 1));
      assertEquals(tensor, Tensors.fromString("{1/2}"));
    }
    {
      Tensor tensor = GeodesicCenter.Splits.of(BinomialWeights.INSTANCE.apply(2 * 2 + 1));
      assertEquals(tensor, Tensors.fromString("{4/5, 3/8}"));
    }
    {
      Tensor tensor = GeodesicCenter.Splits.of(BinomialWeights.INSTANCE.apply(3 * 2 + 1));
      assertEquals(tensor, Tensors.fromString("{6/7, 15/22, 5/16}"));
    }
  }

  public void testFailEven() {
    AssertFail.of(() -> GeodesicCenter.Splits.of(Tensors.vector(1, 2)));
  }

  public void testNonSymmetric() {
    AssertFail.of(() -> GeodesicCenter.Splits.of(Tensors.vector(1, 2, 2)));
  }

  public void testSplitsEvenFail() {
    Splits splits = new GeodesicCenter.Splits(UniformWindowSampler.of(GaussianWindow.FUNCTION));
    splits.apply(5);
    AssertFail.of(() -> splits.apply(4));
  }

  public void testSplitsNullFail() {
    AssertFail.of(() -> new GeodesicCenter.Splits(null));
  }
}
