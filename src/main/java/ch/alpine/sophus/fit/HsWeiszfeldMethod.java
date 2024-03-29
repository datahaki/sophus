// code by jph
package ch.alpine.sophus.fit;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Chop;

/** iterative method to find solution to Fermat-Weber Problem
 * iteration based on Endre Vaszonyi Weiszfeld
 * 
 * <p>implementation based on
 * "Weiszfeld’s Method: Old and New Results"
 * by Amir Beck, Shoham Sabach
 * 
 * @param biinvariantMean
 * @param sedarim for instance InverseDistanceWeighting
 * @param chop */
public record HsWeiszfeldMethod(BiinvariantMean biinvariantMean, Sedarim sedarim, Chop chop) //
    implements SpatialMedian, Serializable {

  private static final int MAX_ITERATIONS = 512;
  // ---
  public HsWeiszfeldMethod {
    Objects.requireNonNull(biinvariantMean);
    Objects.requireNonNull(sedarim);
    Objects.requireNonNull(chop);
  }

  @Override // from SpatialMedian
  public Optional<Tensor> uniform(Tensor sequence) {
    return minimum(sequence, t -> t);
  }

  @Override // from SpatialMedian
  public Optional<Tensor> weighted(Tensor sequence, Tensor weights) {
    return minimum(sequence, Times.operator(weights));
  }

  private Optional<Tensor> minimum(Tensor sequence, UnaryOperator<Tensor> unaryOperator) {
    Tensor equalw = AveragingWeights.of(sequence.length());
    Tensor point = biinvariantMean.mean(sequence, NormalizeTotal.FUNCTION.apply(unaryOperator.apply(equalw)));
    for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
      Tensor weights = sedarim.sunder(point);
      if (chop.isClose(point, point = biinvariantMean.mean(sequence, NormalizeTotal.FUNCTION.apply(unaryOperator.apply(weights)))))
        return Optional.of(point);
    }
    return Optional.empty();
  }
}
