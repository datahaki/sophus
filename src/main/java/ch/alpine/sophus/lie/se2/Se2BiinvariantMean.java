// code by jph, ob
package ch.alpine.sophus.lie.se2;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Int;

/** no restrictions on input points from Covering SE(2), albeit isolated singularities exists
 * 
 * weights are required to be affine
 * 
 * @param lieGroup either se2 or se2c
 * @param scalarBiinvariantMean */
public record Se2BiinvariantMean(Se2CoveringGroup lieGroup, BiinvariantMean scalarBiinvariantMean) implements BiinvariantMean, Serializable {
  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Scalar amean = (Scalar) scalarBiinvariantMean.mean(sequence.get(Tensor.ALL, 2), weights);
    Tensor _00a = sequence.get(0).extract(0, 2).maps(Scalar::zero).append(amean);
    TensorUnaryOperator lieGroupElement = lieGroup.diffOp(_00a);
    Int i = new Int();
    Tensor tmean = sequence.stream() // transform elements in sequence so that angles average to 0
        .map(lieGroupElement) //
        .map(xya -> Se2Skew.of(xya, weights.Get(i.getAndIncrement()))) //
        .reduce(Se2Skew::add) //
        .orElseThrow().solve();
    return lieGroup.combine(_00a, tmean.append(amean.zero()));
  }
}
