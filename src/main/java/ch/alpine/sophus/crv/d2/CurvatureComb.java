// code by jph
package ch.alpine.sophus.crv.d2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.alpine.sophus.hs.r2.SignedCurvature2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.InvertUnlessZero;

/** .
 * G0 - Position, tangent of curve is not continuous, example: polygons
 * G1 - Tangent, curvature is discontinuous, example: Dubins path
 * G2 - Curvature, curvature is continuous but not regular, cubic B-spline
 * G3 - Curvature is regular
 *
 * source:
 * <a href="http://www.aliasworkbench.com/theoryBuilders/images/CombPlot4.jpg">CombPlot4</a>
 *
 * @see Curvature2D */
public enum CurvatureComb {
  ;
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  /** @param tensor with dimensions n x 2 with points of curve
   * @param scaling
   * @param cyclic
   * @return tensor + normal * curvature * scaling */
  public static Tensor of(Tensor tensor, Scalar scaling, boolean cyclic) {
    if (Tensors.isEmpty(tensor))
      return Tensors.empty();
    Tensor normal = cyclic //
        ? cyclic(tensor)
        : string(tensor);
    return tensor.add(normal.multiply(scaling));
  }

  /** @param tensor of dimension n x 2
   * @return normals of dimension n x 2 scaled according to {@link SignedCurvature2D} */
  @PackageTestAccess
  static Tensor string(Tensor tensor) {
    return Times.of(Curvature2D.string(tensor), Normal2D.string(tensor));
  }

  /** @param tensor of dimension n x 2
   * @return normals of dimension n x 2 scaled according to {@link SignedCurvature2D} */
  @PackageTestAccess
  static Tensor cyclic(Tensor tensor) {
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(length);
    if (0 < length) {
      Tensor p = Last.of(tensor);
      Tensor q = tensor.get(0);
      for (int index = 1; index <= length; ++index) {
        Tensor r = tensor.get(index % length);
        list.add(normal(p, q, r, r.subtract(p)));
        p = q;
        q = r;
      }
    }
    Integers.requireEquals(list.size(), length);
    return Unprotect.using(list);
  }

  /** all parameters must have the same unit
   * 
   * @param p
   * @param q
   * @param r
   * @param tangent typically r - p
   * @return */
  @PackageTestAccess
  static Tensor normal(Tensor p, Tensor q, Tensor r, Tensor tangent) {
    Optional<Scalar> optional = SignedCurvature2D.of(p, q, r);
    
    return optional.isPresent() //
        ? NORMALIZE_UNLESS_ZERO.apply(Cross.of(tangent)).multiply(optional.orElseThrow())
        : tangent.map(Unprotect::zero_negateUnit);
  }
}
