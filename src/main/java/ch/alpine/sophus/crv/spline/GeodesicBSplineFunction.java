// code by jph
package ch.alpine.sophus.crv.spline;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.itp.BSplineFunctionBase;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

/** for uniform knot spacing, the implementation matches
 * {@link BSplineFunctionBase#string(int, Tensor)} */
// TODO should share more code with BSplineFunction!!!
public class GeodesicBSplineFunction implements ScalarTensorFunction {
  private static final int CACHE_SIZE = 16;

  /** the control point are stored by reference, i.e. modifications to
   * given tensor alter the behavior of this BSplineFunction instance.
   * 
   * @param binaryAverage
   * @param degree of polynomial basis function, non-negative integer
   * @param sequence of control points with at least one element
   * @return */
  public static GeodesicBSplineFunction of(BinaryAverage binaryAverage, int degree, Tensor sequence) {
    return new GeodesicBSplineFunction( //
        Objects.requireNonNull(binaryAverage), //
        Integers.requirePositiveOrZero(degree), //
        Range.of(0, sequence.length()), //
        sequence);
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
    return new GeodesicBSplineFunction( //
        Objects.requireNonNull(binaryAverage), //
        Integers.requirePositiveOrZero(degree), //
        VectorQ.requireLength(knots, sequence.length()), //
        sequence);
  }

  // ---
  private final Cache<Integer, DeBoor> cache = Cache.of(new Inner(), CACHE_SIZE);
  private final BinaryAverage binaryAverage;
  private final int degree;
  private final Tensor sequence;
  /** half == degree / 2 */
  private final int half;
  /** index of last control point */
  private final int last;
  /** domain of this function */
  private final Clip domain;
  private final NavigableMap<Scalar, Integer> navigableMap;
  private final Tensor samples;

  private GeodesicBSplineFunction(BinaryAverage binaryAverage, int degree, Tensor knots, Tensor sequence) {
    this.binaryAverage = binaryAverage;
    this.degree = degree;
    this.sequence = sequence;
    half = degree / 2;
    Scalar shift = Integers.isEven(degree) //
        ? RationalScalar.HALF
        : RealScalar.ZERO;
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

  /** @param k in the interval [0, control.length() - 1]
   * @return */
  public DeBoor deBoor(int k) {
    return cache.apply(k);
  }

  private class Inner implements Function<Integer, DeBoor>, Serializable {
    @Override
    public DeBoor apply(Integer k) {
      int hi = degree + 1 + k;
      return new DeBoor( //
          binaryAverage, // 
          degree, //
          getKnots(k), //
//          samples.extract(k, k + 2 * degree), //
          Tensor.of(IntStream.range(k - half, hi - half) // control
              .map(GeodesicBSplineFunction.this::bound) //
              .mapToObj(sequence::get)));
    }
  }
  
  protected Tensor getKnots(int k) {
    return samples.extract(k, k + 2 * degree);  
  }


  // helper function
  private int bound(int index) {
    return Math.min(Math.max(0, index), last);
  }
}
