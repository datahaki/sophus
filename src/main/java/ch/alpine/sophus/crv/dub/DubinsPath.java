// code by jph
package ch.alpine.sophus.crv.dub;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.itp.ArcLengthParameterization;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2c.Se2CoveringIntegrator;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Accumulate;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Sign;

/** compatible with the use of Quantity:
 * radius and entries in segLength must have the same unit
 * 
 * immutable */
public class DubinsPath implements Serializable {
  /** @param type non-null
   * @param radius strictly positive
   * @param segLength {length1, length2, length3} each non-negative
   * @return */
  public static DubinsPath of(DubinsType type, Scalar radius, Tensor segLength) {
    return new DubinsPath( //
        Objects.requireNonNull(type), //
        Sign.requirePositive(radius), //
        VectorQ.requireLength(segLength, 3), //
        segLength.stream() //
            .map(Scalar.class::cast) //
            .map(Sign::requirePositiveOrZero) //
            .reduce(Scalar::add).orElseThrow());
  }

  // ---
  private final DubinsType type;
  private final Scalar radius;
  private final Tensor segLength;
  private final Scalar length;

  /** @param type non-null
   * @param radius strictly positive
   * @param segLength {length1, length2, length3} each non-negative
   * @param length */
  private DubinsPath(DubinsType type, Scalar radius, Tensor segLength, Scalar length) {
    this.type = type;
    this.radius = radius;
    this.segLength = segLength;
    this.length = length;
  }

  /** @return dubins path type */
  public DubinsType type() {
    return type;
  }

  /** @return vector of length 3 with parameter values of transition points */
  public Tensor segments() {
    return Accumulate.of(segLength);
  }

  /** @return total length of Dubins path in Euclidean space */
  public Scalar length() {
    return length;
  }

  /** @param index is 0, 1, or 2
   * @return length of segment of given index */
  public Scalar length(int index) {
    return segLength.Get(index);
  }

  /** @return total curvature, return value is non-negative */
  public Scalar totalCurvature() {
    return (Scalar) segLength.dot(type.signatureAbs()).divide(radius);
  }

  /** @param g start configuration
   * @return arc-length parameterization of dubins path starting at given configuration g
   * over the closed interval [0, 1] */
  public ScalarTensorFunction unit(Tensor g) {
    if (Scalars.isZero(length))
      return zero(g);
    Tensor tensor = Tensors.reserve(4);
    tensor.append(g);
    for (int index = 0; index < 3; ++index)
      tensor.append(g = Se2CoveringIntegrator.INSTANCE.spin(g, type.tangent(index, radius).multiply(segLength.Get(index))));
    return ArcLengthParameterization.of(segLength, Se2CoveringGroup.INSTANCE, tensor);
  }

  /** @param g start configuration
   * @return arc-length parameterization of dubins path starting at given configuration g
   * over the closed interval [length().zero(), length()] */
  public ScalarTensorFunction sampler(Tensor g) {
    if (Scalars.isZero(length))
      return zero(g);
    ScalarTensorFunction scalarTensorFunction = unit(g);
    return scalar -> scalarTensorFunction.apply(scalar.divide(length));
  }

  private static ScalarTensorFunction zero(Tensor g) {
    return scalar -> g.copy();
  }
}
