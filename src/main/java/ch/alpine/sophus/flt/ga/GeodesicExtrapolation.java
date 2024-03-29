// code by ob
package ch.alpine.sophus.flt.ga;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.win.HalfWindowSampler;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.itp.BinaryAverage;

/** GeodesicExtrapolate projects a sequence of points to their next (expected) point
 * with each point weighted as provided by an external function. */
public class GeodesicExtrapolation implements TensorUnaryOperator {
  /** @param binaryAverage
   * @param function that maps an extent to a weight mask of length "sequence.length - 2"
   * @return operator that maps a sequence of number of points to their next (expected) point
   * @throws Exception if either input parameters is null */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, Function<Integer, Tensor> function) {
    return new GeodesicExtrapolation(binaryAverage, Objects.requireNonNull(function));
  }

  /** @param binaryAverage
   * @param windowFunction
   * @return operator that maps a sequence of number of points to their next (expected) point
   * @throws Exception if either input parameters is null */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, ScalarUnaryOperator windowFunction) {
    return new GeodesicExtrapolation(binaryAverage, HalfWindowSampler.of(windowFunction));
  }

  // ---
  @PackageTestAccess
  static class Splits implements Function<Integer, Tensor>, Serializable {
    private final Function<Integer, Tensor> function;

    private Splits(Function<Integer, Tensor> function) {
      this.function = function;
    }

    @Override
    public Tensor apply(Integer t) {
      return of(function.apply(t));
    }

    /** @param mask affine
     * @return Tensor [i1, ..., in, e] with i being interpolatory weights and e the extrapolation weight
     * @throws Exception if mask is not affine */
    @PackageTestAccess
    static Tensor of(Tensor mask) {
      // check for affinity
      AffineQ.require(mask);
      // no extrapolation possible
      if (mask.length() == 1)
        return Tensors.vector(1);
      Tensor splits = Tensors.empty(); // TODO SOPHUS IMPL use reserve
      Scalar factor = mask.Get(0);
      // Calculate interpolation splits
      for (int index = 1; index < mask.length() - 1; ++index) {
        factor = factor.add(mask.Get(index));
        Scalar lambda = mask.Get(index).divide(factor);
        splits.append(lambda);
      }
      // Calculate extrapolation splits
      Scalar temp = RealScalar.ONE;
      for (int index = 0; index < splits.length(); index++) {
        temp = temp.multiply(RealScalar.ONE.subtract(splits.Get(index))).add(RealScalar.ONE);
      }
      splits.append(RealScalar.ONE.add(temp.reciprocal()));
      return splits;
    }
  }

  // ---
  private final BinaryAverage binaryAverage;
  private final Function<Integer, Tensor> function;

  private GeodesicExtrapolation(BinaryAverage binaryAverage, Function<Integer, Tensor> function) {
    this.binaryAverage = Objects.requireNonNull(binaryAverage);
    this.function = Cache.of(new Splits(function), 32);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    Tensor splits = function.apply(tensor.length());
    Tensor result = tensor.get(0);
    for (int index = 1; index < tensor.length(); ++index)
      result = binaryAverage.split(result, tensor.get(index), splits.Get(index - 1));
    return result;
  }
}
