// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.itp.AbstractInterpolation;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.sca.Floor;

/** In general, the function {@link #get(Tensor)} has to be called with exactly the number
 * of indices as correspond to the number of grid dimensions provided in given tensor.
 * 
 * Hint:
 * for {@link RnBiinvariantMean} the interpolation is identical to {@link LinearInterpolation} */
/* package */ class BiinvariantMeanInterpolation extends AbstractInterpolation implements Serializable {
  /** @param biinvariantMean
   * @param tensor
   * @return */
  public static Interpolation of(BiinvariantMean biinvariantMean, Tensor tensor) {
    return new BiinvariantMeanInterpolation(Objects.requireNonNull(biinvariantMean), tensor);
  }

  // ---
  private final BiinvariantMean biinvariantMean;
  private final Tensor tensor;

  private BiinvariantMeanInterpolation(BiinvariantMean biinvariantMean, Tensor tensor) {
    this.biinvariantMean = biinvariantMean;
    this.tensor = Unprotect.references(tensor);
  }

  @Override // from Interpolation
  public Tensor get(Tensor index) {
    // LONGTERM implementation fails when any entry in index corresponds to exactly
    // the length of tensor in the respective dimensions
    int n = index.length();
    Tensor findex = Floor.of(index);
    Tensor weights = Flatten.of(index.subtract(findex).stream() //
        .map(scalar -> Tensors.of(RealScalar.ONE.subtract(scalar), scalar)) //
        .reduce(TensorProduct::of) //
        .orElseThrow(), n - 1);
    List<Integer> fromIndex = findex.stream() //
        .map(Scalar.class::cast) //
        .map(Scalar::number) //
        .map(Number::intValue) //
        .collect(Collectors.toList());
    List<Integer> dimensions = Collections.nCopies(n, 2);
    Tensor sequence = Flatten.of(tensor.block(fromIndex, dimensions), n - 1);
    return biinvariantMean.mean(sequence, weights);
  }

  @Override // from Interpolation
  public Tensor at(Scalar index) {
    Scalar floor = Floor.FUNCTION.apply(index);
    Scalar remain = index.subtract(floor);
    int below = floor.number().intValue();
    if (Scalars.isZero(remain))
      return tensor.get(below);
    return biinvariantMean.mean( //
        tensor.extract(below, below + 2), // sequence
        Tensors.of(RealScalar.ONE.subtract(remain), remain));
  }
}
