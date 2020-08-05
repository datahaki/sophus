// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
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
public class D2BarycentricCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold with 2-dimensional tangent space
   * @param biFunction {@link Barycenter}
   * @return */
  public static BarycentricCoordinate of( //
      VectorLogManifold vectorLogManifold, //
      BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new D2BarycentricCoordinate( //
        Objects.requireNonNull(vectorLogManifold), //
        Objects.requireNonNull(biFunction));
  }

  /***************************************************/
  private final BiFunction<Tensor, Scalar, Tensor> biFunction;
  private final VectorLogManifold vectorLogManifold;

  private D2BarycentricCoordinate( //
      VectorLogManifold vectorLogManifold, //
      BiFunction<Tensor, Scalar, Tensor> biFunction) {
    this.vectorLogManifold = vectorLogManifold;
    this.biFunction = biFunction;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    int length = sequence.length();
    Tensor[] auxs = new Tensor[length];
    Scalar[] dens = new Scalar[length];
    int ind = 0;
    TangentSpace tangentSpace = vectorLogManifold.logAt(point);
    for (Tensor p : sequence) {
      Tensor dif = tangentSpace.vectorLog(p); // dif is vector of length 2
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
