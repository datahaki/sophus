// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.math.Det2D;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Hypot;

/** Three-point coordinates are also referred to as "Complete family of coordinates"
 * 
 * References:
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public class ThreePointCoordinate implements TensorUnaryOperator {
  private static final long serialVersionUID = -4726321956728082729L;

  /** @param biFunction
   * @return */
  public static TensorUnaryOperator of(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new ThreePointCoordinate(Objects.requireNonNull(biFunction));
  }

  /***************************************************/
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;

  private ThreePointCoordinate(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.biFunction = biFunction;
  }

  @Override
  public Tensor apply(Tensor levers) {
    int length = levers.length();
    Tensor[] auxs = new Tensor[length];
    Scalar[] dens = new Scalar[length];
    int ind = 0;
    for (Tensor dif : levers) { // dif is vector of length 2
      Scalar den = Hypot.ofVector(dif); // use of metric
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
    return NormalizeTotal.FUNCTION.apply(tensor);
  }

  private static Scalar forward(Tensor ofs1, Tensor ofs2) {
    Scalar den = Det2D.of(ofs1, ofs2); // signed area computation in oriented plane
    return Scalars.isZero(den) //
        ? RealScalar.ZERO
        : ofs2.subtract(ofs1).dot(ofs2).Get().divide(den);
  }
}
