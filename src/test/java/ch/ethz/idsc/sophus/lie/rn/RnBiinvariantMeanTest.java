// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import java.util.Optional;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.ProjectedCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class RnBiinvariantMeanTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Total::ofVector);

  public void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(3));
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NORMALIZE.apply(RandomVariate.of(distribution, length));
    Tensor mean = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
  }

  public void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(10, 100);
    int length = 10;
    Tensor sequence = RandomVariate.of(distribution, length, 3);
    Tensor weights = NORMALIZE.apply(RandomVariate.of(distribution, length));
    Tensor mean = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._14.requireClose(mean, weights.dot(sequence));
    ExactTensorQ.require(mean);
  }

  public void testEmpty() {
    try {
      RnBiinvariantMean.INSTANCE.mean(Tensors.empty(), Tensors.empty());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(RnManifold.HS_EXP);
  private static final ProjectedCoordinate[] PROJECTED_COORDINATES = { //
      AbsoluteCoordinate.linear(RnManifold.INSTANCE), //
      AbsoluteCoordinate.smooth(RnManifold.INSTANCE) };

  public void testSimple2() {
    MeanDefect meanDefect = BiinvariantMeanDefect.of(RnManifold.HS_EXP);
    Tensor sequence = Tensors.of( //
        RnExponential.INSTANCE.exp(Tensors.vector(+1 + 0.3, 0, 0)), //
        RnExponential.INSTANCE.exp(Tensors.vector(+0 + 0.3, 0, 0)), //
        RnExponential.INSTANCE.exp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = meanDefect.defect( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), RnExponential.INSTANCE.exp(Tensors.vector(+0.3, 0, 0)));
    Chop._10.requireAllZero(log);
  }

  public void testConvergence() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES)
      for (int n = 3; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(RnExponential.INSTANCE::exp));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
        Optional<Tensor> optional = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, weights);
        Tensor mean = optional.get();
        Tensor w2 = projectedCoordinate.weights(sequence, mean);
        Optional<Tensor> o2 = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, w2);
        Chop._08.requireClose(mean, o2.get());
      }
  }

  public void testConvergenceExact() {
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    int n = 4;
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES) {
      Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(RnExponential.INSTANCE::exp));
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), n));
      Optional<Tensor> optional = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, weights);
      Tensor mean = optional.get();
      Tensor w2 = projectedCoordinate.weights(sequence, mean);
      Optional<Tensor> o2 = ITERATIVE_BIINVARIANT_MEAN.apply(sequence, w2);
      Chop._08.requireClose(mean, o2.get());
      Chop._08.requireClose(weights, w2);
    }
  }
}
