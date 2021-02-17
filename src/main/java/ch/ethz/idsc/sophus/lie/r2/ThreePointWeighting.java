// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

/** Three-point homogeneous weights:
 * weighting satisfies barycentric equation but do not necessarily sum up to one. */
public class ThreePointWeighting implements Genesis, Serializable {
  /** @param biFunction
   * @return */
  public static Genesis of(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new ThreePointWeighting(Objects.requireNonNull(biFunction));
  }

  /***************************************************/
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;

  private ThreePointWeighting(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.biFunction = biFunction;
  }

  @Override
  public Tensor origin(Tensor levers) {
    int length = levers.length();
    Tensor[] auxs = new Tensor[length];
    Scalar[] dens = new Scalar[length];
    int ind = 0;
    for (Tensor dif : levers) { // dif is vector of length 2
      Scalar den = Vector2Norm.of(dif); // use of metric
      if (Scalars.isZero(den))
        return UnitVector.of(length, ind);
      auxs[ind] = biFunction.apply(dif, den);
      dens[ind] = den;
      ++ind;
    }
    Tensor tensor = Tensors.reserve(length);
    for (int index = 0; index < length; ++index) {
      Tensor prev = auxs[(index + length - 1) % length];
      Tensor cntr = auxs[index];
      Tensor next = auxs[(index + 1) % length];
      Scalar diff = forward(cntr, next).subtract(forward(cntr, prev));
      tensor.append(biFunction.apply(diff, dens[index]));
    }
    return tensor;
  }

  private static Scalar forward(Tensor ofs1, Tensor ofs2) {
    Scalar den = Det2D.of(ofs1, ofs2); // signed area computation in oriented plane
    return Scalars.isZero(den) //
        ? RealScalar.ZERO
        : (Scalar) ofs2.subtract(ofs1).dot(ofs2).divide(den);
  }
}
