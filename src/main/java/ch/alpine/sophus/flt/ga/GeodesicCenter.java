// code by jph
package ch.alpine.sophus.flt.ga;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.flt.CenterFilter;
import ch.alpine.sophus.math.SymmetricVectorQ;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.itp.BinaryAverage;

/** GeodesicCenter projects a sequence of points to their geodesic center
 * with each point weighted as provided by an external function.
 * 
 * <p>Careful: the implementation only supports sequences with ODD number of elements!
 * When a sequence of even length is provided an Exception is thrown.
 * 
 * @see CenterFilter */
public class GeodesicCenter implements TensorUnaryOperator {
  private static final int CACHE_SIZE = 24;

  /** @param binaryAverage
   * @param function that maps an extent to a weight mask of length == 2 * extent + 1
   * @return operator that maps a sequence of odd number of points to their geodesic center
   * @throws Exception if either input parameter is null */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, Function<Integer, Tensor> function) {
    return new GeodesicCenter(binaryAverage, function);
  }

  /** Example:
   * <pre>
   * GeodesicCenter.of(Se2Geodesic.INSTANCE, GaussianWindow.FUNCTION);
   * </pre>
   * 
   * @param binaryAverage
   * @param windowFunction
   * @return
   * @throws Exception if either input parameter is null */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, ScalarUnaryOperator windowFunction) {
    return of(binaryAverage, UniformWindowSampler.of(windowFunction));
  }

  // ---
  @PackageTestAccess
  static class Splits implements Function<Integer, Tensor>, Serializable {
    private final Function<Integer, Tensor> function;

    public Splits(Function<Integer, Tensor> function) {
      this.function = Objects.requireNonNull(function);
    }

    @Override
    public Tensor apply(Integer t) {
      return of(function.apply(t));
    }

    /** @param mask symmetric vector of odd length
     * @return weights of Kalman-style iterative moving average
     * @throws Exception if mask is not symmetric or has even number of elements */
    @PackageTestAccess
    static Tensor of(Tensor mask) {
      if (Integers.isEven(mask.length()))
        throw Throw.of(mask);
      SymmetricVectorQ.require(mask);
      int radius = (mask.length() - 1) / 2;
      Tensor halfmask = Tensors.vector(i -> i == 0 //
          ? mask.Get(radius + i)
          : mask.Get(radius + i).multiply(RealScalar.TWO), radius);
      Scalar factor = RealScalar.ONE;
      Tensor splits = Tensors.reserve(radius);
      for (int index = 0; index < radius; ++index) {
        Scalar lambda = halfmask.Get(index).divide(factor);
        splits.append(lambda);
        factor = factor.multiply(RealScalar.ONE.subtract(lambda));
      }
      return Reverse.of(splits);
    }
  }

  // ---
  private final BinaryAverage binaryAverage;
  private final Function<Integer, Tensor> function;

  private GeodesicCenter(BinaryAverage binaryAverage, Function<Integer, Tensor> function) {
    this.binaryAverage = Objects.requireNonNull(binaryAverage);
    this.function = Cache.of(new Splits(function), CACHE_SIZE);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    if (Integers.isEven(tensor.length()))
      throw Throw.of(tensor);
    // spatial neighborhood we want to consider for centering
    int radius = (tensor.length() - 1) / 2;
    Tensor splits = function.apply(tensor.length());
    Tensor pL = tensor.get(0);
    Tensor pR = tensor.get(2 * radius);
    for (int index = 0; index < radius;) {
      Scalar scalar = splits.Get(index++);
      pL = binaryAverage.split(pL, tensor.get(index), scalar);
      Tensor lR = tensor.get(2 * radius - index);
      pR = binaryAverage.split(lR, pR, RealScalar.ONE.subtract(scalar));
    }
    return binaryAverage.split(pL, pR, RationalScalar.HALF);
  }
}
