// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.itp.Interpolation;
import ch.ethz.idsc.tensor.itp.LinearInterpolation;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantMeanInterpolationTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor vector = RandomVariate.of(UniformDistribution.unit(), 12);
    Interpolation bi = Serialization.copy(BiinvariantMeanInterpolation.of(RnBiinvariantMean.INSTANCE, vector));
    Interpolation li = LinearInterpolation.of(vector);
    Distribution distribution = UniformDistribution.of(0, vector.length() - 1);
    Tensor domain = RandomVariate.of(distribution, 100);
    Tensor bv = domain.map(bi::at);
    Tensor lv = domain.map(li::at);
    Chop._12.requireClose(bv, lv);
  }

  public void testExact() {
    Tensor vector = RandomVariate.of(DiscreteUniformDistribution.of(10, 20), 12);
    Interpolation interpolation = BiinvariantMeanInterpolation.of(RnBiinvariantMean.INSTANCE, vector);
    Tensor result = Range.of(0, 12).map(interpolation::at);
    assertEquals(result, vector);
    ExactTensorQ.require(result);
  }

  public void testMultiRn() {
    Tensor tensor = Array.of(Tensors::vector, 3, 2, 4);
    Interpolation interp1 = BiinvariantMeanInterpolation.of(RnBiinvariantMean.INSTANCE, tensor);
    Interpolation interp2 = LinearInterpolation.of(tensor);
    Tensor index = Tensors.vector(1.25, 0.45, 2.125);
    Tensor res1 = interp1.get(index);
    Tensor res2 = interp2.get(index);
    Chop._10.requireClose(res1, res2);
  }

  public void testMultiSe2() {
    Distribution distribution = NormalDistribution.standard();
    Tensor tensor = RandomVariate.of(distribution, 4, 4, 3);
    Interpolation interp1 = BiinvariantMeanInterpolation.of(Se2CoveringBiinvariantMean.INSTANCE, tensor);
    AssertFail.of(() -> interp1.get(Tensors.vector(1.2)));
    Tensor tensor2 = interp1.get(Tensors.vector(1.2, 2.3));
    VectorQ.requireLength(tensor2, 3);
    Tolerance.CHOP.requireClose(interp1.get(Tensors.vector(0, 0)), tensor.get(0, 0));
    Tolerance.CHOP.requireClose(interp1.get(Tensors.vector(1, 1)), tensor.get(1, 1));
    Tolerance.CHOP.requireClose(interp1.get(Tensors.vector(0, 2)), tensor.get(0, 2));
    Tolerance.CHOP.requireClose(interp1.get(Tensors.vector(1, 2)), tensor.get(1, 2));
    Tolerance.CHOP.requireClose(interp1.get(Tensors.vector(2, 2)), tensor.get(2, 2));
  }
}
