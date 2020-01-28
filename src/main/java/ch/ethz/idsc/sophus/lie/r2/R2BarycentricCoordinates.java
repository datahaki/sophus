// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.math.Det2D;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Hypot;

/** Reference:
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public class R2BarycentricCoordinates implements Serializable {
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;

  /** @param biFunction
   * @see Barycenter */
  public R2BarycentricCoordinates(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.biFunction = Objects.requireNonNull(biFunction);
  }

  /** function takes vector x of length 2 that is strictly inside polygon and
   * returns vector that satisfies the equation: vector dot polygon == x
   * 
   * @param polygon non-empty
   * @return
   * @throws Exception if polygon is empty */
  public TensorUnaryOperator of(Tensor polygon) {
    if (Unprotect.dimension1(polygon) == 2)
      return new Dual(polygon);
    throw TensorRuntimeException.of(polygon);
  }

  private class Dual implements TensorUnaryOperator {
    private final Tensor[] polygon;
    private final int length;

    public Dual(Tensor polygon) {
      this.polygon = polygon.stream().toArray(Tensor[]::new);
      length = polygon.length();
    }

    @Override
    public Tensor apply(Tensor x) {
      Tensor[] auxs = new Tensor[length];
      Scalar[] dens = new Scalar[length];
      for (int index = 0; index < length; ++index) {
        Tensor dif = polygon[index].subtract(x);
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
        Scalar diff = forward(cntr, next).subtract(forward(cntr, prev));
        tensor.append(biFunction.apply(diff, dens[index]));
      }
      return NormalizeTotal.FUNCTION.apply(tensor);
    }
  }

  private static Scalar forward(Tensor ofs1, Tensor ofs2) {
    Scalar den = Det2D.of(ofs1, ofs2);
    return Scalars.isZero(den) //
        ? RealScalar.ZERO
        : ofs2.subtract(ofs1).dot(ofs2).Get().divide(den);
  }
}
