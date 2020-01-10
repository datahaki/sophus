// code by jph
package ch.ethz.idsc.sophus.ply.crd;

import java.io.Serializable;
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
import ch.ethz.idsc.tensor.red.Hypot;

/** Reference:
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public class PowerCoordinates implements Serializable {
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;

  /** @param biFunction
   * @see Barycentric */
  public PowerCoordinates(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.biFunction = Objects.requireNonNull(biFunction);
  }

  /** @param polygon convex
   * @param x strictly inside polygon
   * @return vector with length of polygon */
  public Tensor weights(Tensor polygon, Tensor x) {
    return NormalizeTotal.FUNCTION.apply(hDual(polygon, x));
  }

  Tensor hDual(Tensor polygon, Tensor x) {
    int length = polygon.length();
    Tensor[] auxs = new Tensor[length];
    Scalar[] dens = new Scalar[length];
    Tensor pres = Tensor.of(polygon.stream().map(x::subtract));
    for (int index = 0; index < length; ++index) {
      Tensor dif = pres.get(index);
      Scalar den = Hypot.ofVector(dif);
      if (Scalars.isZero(den))
        return UnitVector.of(length, index);
      auxs[index] = biFunction.apply(dif, den);
      dens[index] = den;
    }
    Tensor tensor = Tensors.reserve(length);
    for (int index = 0; index < length; ++index) {
      Tensor prev = auxs[(index + length - 1) % length];
      Tensor cntr = auxs[index];
      Tensor next = auxs[(index + 1) % length];
      Scalar diff = forward(cntr, prev).subtract(forward(cntr, next)).abs();
      tensor.append(diff.multiply(Hypot.ofVector(cntr)).divide(dens[index]));
    }
    return tensor;
  }

  Scalar forward(Tensor ofs1, Tensor ofs2) {
    Scalar den = Det2D.of(ofs1, ofs2);
    return Scalars.isZero(den) //
        ? RealScalar.ZERO
        : ofs2.subtract(ofs1).dot(ofs2).Get().divide(den);
  }
}
