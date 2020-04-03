// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.Det2D;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.red.Hypot;

/** References:
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016
 * 
 * @see Barycenter */
public class R2BarycentricCoordinate implements BarycentricCoordinate, Serializable {
  /** @param biFunction for instance {@link Barycenter#MEAN_VALUE} */
  public static BarycentricCoordinate of(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new R2BarycentricCoordinate(biFunction);
  }

  /***************************************************/
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;

  private R2BarycentricCoordinate(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.biFunction = Objects.requireNonNull(biFunction);
  }

  /** function takes vector x of length 2 that is strictly inside polygon and
   * returns vector that satisfies the equation: vector dot polygon == x
   * 
   * @param polygon non-empty matrix of dimensions n x 2
   * @param x vector of length 2
   * @return vector of length n
   * @throws Exception if polygon is empty */
  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor polygon, Tensor x) {
    int length = polygon.length();
    Tensor[] auxs = new Tensor[length];
    Scalar[] dens = new Scalar[length];
    int ind = 0;
    for (Tensor p : polygon) {
      Tensor dif = p.subtract(x);
      Scalar den = Hypot.ofVector(dif);
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
    Scalar den = Det2D.of(ofs1, ofs2);
    return Scalars.isZero(den) //
        ? RealScalar.ZERO
        : ofs2.subtract(ofs1).dot(ofs2).Get().divide(den);
  }
}
