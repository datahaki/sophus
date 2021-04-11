// code by jph
package ch.ethz.idsc.sophus.fit;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.red.ArgMin;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.N;

/** iterative method to find solution to Fermat-Weber Problem
 * iteration based on Endre Vaszonyi Weiszfeld
 * 
 * <p>implementation based on
 * "Weiszfeld’s Method: Old and New Results"
 * by Amir Beck, Shoham Sabach */
public class WeiszfeldMethod implements SpatialMedian, Serializable {
  private static final long serialVersionUID = -555862284852117669L;
  private static final int MAX_ITERATIONS = 512;

  /** @param chop non null
   * @return */
  public static SpatialMedian with(Chop chop) {
    return new WeiszfeldMethod(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  /** @param chop */
  private WeiszfeldMethod(Chop chop) {
    this.chop = chop;
  }

  @Override // from SpatialMedian
  public Optional<Tensor> uniform(Tensor sequence) {
    return weighted(sequence, AveragingWeights.of(sequence.length()));
  }

  @Override // from SpatialMedian
  public Optional<Tensor> weighted(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    Tensor point = N.DOUBLE.of(weights.dot(sequence)); // initial value
    for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
      Tensor dist = Tensor.of(sequence.stream().map(point.negate()::add).map(Vector2Norm::of));
      int index = ArgMin.of(dist);
      if (Scalars.isZero(dist.Get(index)))
        return Optional.of(point);
      Tensor invdist = dist.map(Scalar::reciprocal);
      if (chop.isClose(point, point = NormalizeTotal.FUNCTION.apply(weights.pmul(invdist)).dot(sequence)))
        return Optional.of(point);
    }
    return Optional.empty();
  }
}
