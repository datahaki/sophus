// code by jph, ob
package ch.ethz.idsc.sophus.lie.se2c;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.ScalarBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2.Se2Group;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** no restrictions on input points from Covering SE(2), albeit isolated singularities exists
 * 
 * weights are required to be affine */
public class Se2UniversalBiinvariantMean implements BiinvariantMean, Serializable {
  private static final long serialVersionUID = -3740692365906664693L;

  /** @param scalarBiinvariantMean
   * @return */
  public static BiinvariantMean covering(ScalarBiinvariantMean scalarBiinvariantMean) {
    return new Se2UniversalBiinvariantMean(Se2CoveringGroup.INSTANCE, scalarBiinvariantMean);
  }

  /** @param scalarBiinvariantMean
   * @return */
  public static BiinvariantMean se2(ScalarBiinvariantMean scalarBiinvariantMean) {
    return new Se2UniversalBiinvariantMean(Se2Group.INSTANCE, scalarBiinvariantMean);
  }

  /***************************************************/
  private static final Scalar ZERO = RealScalar.ZERO;
  // ---
  private final LieGroup lieGroup;
  private final ScalarBiinvariantMean scalarBiinvariantMean;

  /** @param lieGroup either se2 or se2c
   * @param scalarBiinvariantMean */
  private Se2UniversalBiinvariantMean(LieGroup lieGroup, ScalarBiinvariantMean scalarBiinvariantMean) {
    this.lieGroup = lieGroup;
    this.scalarBiinvariantMean = Objects.requireNonNull(scalarBiinvariantMean);
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Scalar amean = scalarBiinvariantMean.mean(sequence.get(Tensor.ALL, 2), weights);
    LieGroupElement lieGroupElement = lieGroup.element(Tensors.of(ZERO, ZERO, amean));
    AtomicInteger atomicInteger = new AtomicInteger();
    Tensor tmean = sequence.stream() // transform elements in sequence so that angles average to 0
        .map(lieGroupElement.inverse()::combine) //
        .map(xya -> Se2Skew.of(xya, weights.Get(atomicInteger.getAndIncrement()))) //
        .reduce(Se2Skew::add) //
        .get().solve();
    return lieGroupElement.combine(tmean.append(amean.zero()));
  }
}
