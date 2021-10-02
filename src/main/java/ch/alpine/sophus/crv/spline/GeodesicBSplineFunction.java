// code by jph
package ch.alpine.sophus.crv.spline;

import java.util.NavigableMap;
import java.util.TreeMap;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.itp.BSplineFunction;
import ch.alpine.tensor.itp.BSplineFunctionString;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/** for uniform knot spacing, the implementation matches
 * {@link BSplineFunctionString#of(int, Tensor)} */
public class GeodesicBSplineFunction extends BSplineFunction {
  /** the control point are stored by reference, i.e. modifications to
   * given tensor alter the behavior of this BSplineFunction instance.
   * 
   * @param binaryAverage
   * @param degree of polynomial basis function, non-negative integer
   * @param sequence of control points with at least one element
   * @return */
  public static GeodesicBSplineFunction of(BinaryAverage binaryAverage, int degree, Tensor sequence) {
    return new GeodesicBSplineFunction(binaryAverage, degree, //
        Range.of(0, sequence.length()), sequence);
  }

  /** the control point are stored by reference, i.e. modifications to
   * given tensor alter the behavior of this BSplineFunction instance.
   * 
   * @param binaryAverage
   * @param degree of polynomial basis function, non-negative integer
   * @param knots vector of the same length as control
   * @param sequence of control points with at least one element
   * @return
   * @throws Exception */
  public static GeodesicBSplineFunction of(BinaryAverage binaryAverage, int degree, Tensor knots, Tensor sequence) {
    OrderedQ.require(knots);
    return new GeodesicBSplineFunction(binaryAverage, degree, //
        VectorQ.requireLength(knots, sequence.length()), sequence);
  }

  // ---
  /** index of last control point */
  private final int last;
  /** domain of this function */
  private final Clip domain;
  private final NavigableMap<Scalar, Integer> navigableMap;
  private final Tensor samples;

  private GeodesicBSplineFunction(BinaryAverage binaryAverage, int degree, Tensor knots, Tensor sequence) {
    super(binaryAverage, degree, sequence);
    last = sequence.length() - 1;
    domain = Clips.interval(knots.Get(0), Last.of(knots));
    navigableMap = new TreeMap<>();
    navigableMap.put(knots.Get(0), 0);
    for (int index = 1; index < knots.length(); ++index)
      navigableMap.put(Integers.isEven(degree) //
          ? (Scalar) RnGeodesic.INSTANCE.midpoint(knots.Get(index - 1), knots.Get(index))
          : knots.Get(index), index);
    samples = Unprotect.references(Range.of(-degree + 1, sequence.length() + degree) //
        .map(index -> index.subtract(shift)) //
        .map(Clips.interval(0, last)) //
        .map(LinearInterpolation.of(knots)::at));
  }

  /** @param scalar inside interval [0, control.length() - 1]
   * @return
   * @throws Exception if given scalar is outside required interval */
  @Override
  public Tensor apply(final Scalar scalar) {
    return deBoor(scalar).apply(scalar);
  }

  /** @return */
  public Clip domain() {
    return domain;
  }

  public DeBoor deBoor(Scalar scalar) {
    return deBoor(navigableMap.floorEntry(domain.requireInside(scalar)).getValue());
  }

  @Override
  protected Tensor knots(int k) {
    return samples.extract(k, k + 2 * degree);
  }

  @Override
  protected int bound(int index) {
    return Math.min(Math.max(0, index), last);
  }
}
