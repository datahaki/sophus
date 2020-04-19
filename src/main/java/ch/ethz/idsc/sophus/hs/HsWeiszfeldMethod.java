// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.SpatialMedian;
import ch.ethz.idsc.tensor.sca.Chop;

/** iterative method to find solution to Fermat-Weber Problem
 * iteration based on Endre Vaszonyi Weiszfeld
 * 
 * <p>implementation based on
 * "Weiszfeld’s Method: Old and New Results"
 * by Amir Beck, Shoham Sabach */
public class HsWeiszfeldMethod implements SpatialMedian, Serializable {
  private static final int MAX_ITERATIONS = 512;

  /** @param biinvariantMean
   * @param tensorMetric
   * @param chop
   * @return */
  public static SpatialMedian of(BiinvariantMean biinvariantMean, TensorMetric tensorMetric, Chop chop) {
    return new HsWeiszfeldMethod(Objects.requireNonNull(biinvariantMean), tensorMetric, Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final BiinvariantMean biinvariantMean;
  private final WeightingInterface weightingInterface;
  private final Chop chop;

  private HsWeiszfeldMethod(BiinvariantMean biinvariantMean, TensorMetric tensorMetric, Chop chop) {
    this.biinvariantMean = biinvariantMean;
    weightingInterface = InverseDistanceWeighting.of(tensorMetric);
    this.chop = chop;
  }

  @Override // from SpatialMedian
  public Optional<Tensor> uniform(Tensor sequence) {
    return minimum(sequence, t -> t);
  }

  @Override // from SpatialMedian
  public Optional<Tensor> weighted(Tensor sequence, Tensor weights) {
    return minimum(sequence, weights::pmul);
  }

  private Optional<Tensor> minimum(Tensor sequence, UnaryOperator<Tensor> unaryOperator) {
    Tensor equalw = ConstantArray.of(RationalScalar.of(1, sequence.length()), sequence.length());
    Tensor point = biinvariantMean.mean(sequence, NormalizeTotal.FUNCTION.apply(unaryOperator.apply(equalw)));
    int iteration = 0;
    while (++iteration < MAX_ITERATIONS) {
      Tensor prev = point;
      Tensor weights = weightingInterface.weights(sequence, point);
      point = biinvariantMean.mean(sequence, NormalizeTotal.FUNCTION.apply(unaryOperator.apply(weights)));
      if (chop.close(point, prev))
        return Optional.of(point);
    }
    return Optional.empty();
  }
}