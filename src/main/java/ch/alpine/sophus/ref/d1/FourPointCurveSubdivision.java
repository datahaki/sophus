// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.sophus.api.SplitInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.ext.Integers;

/** C1 interpolatory four-point scheme
 * Dubuc 1986, Dyn/Gregory/Levin 1987
 * 
 * <pre>
 * weights = {-1, 9, 9, -1} / 16
 * weights = {-omega, 1/2+omega, 1/2+omega, -omega} (generalized)
 * </pre>
 * 
 * Dyn/Sharon 2014 p.14 show that for general omega the contractivity is mu = 4 * omega + 1/2
 * 
 * for the important case omega = 1/16 the contractivity factor is mu = 3/4
 * 
 * Quote:
 * "We show that when the scheme is used to generate a limit curve that
 * interpolates given irregularly spaced points, sampled from a curve in
 * any space dimension with a bounded fourth derivative, and the chosen
 * parameterization is chordal, the accuracy is fourth order as the mesh
 * size goes to zero. In contrast, uniform and centripetal parameterizations
 * yield only second order."
 * Reference:
 * "The approximation order of four-point interpolatory curve subdivision"
 * by Michael S. Floater */
public class FourPointCurveSubdivision extends BSpline1CurveSubdivision {
  private final static Scalar P1_16 = RationalScalar.of(1, 16);
  private final static Scalar N1_4 = RationalScalar.of(-1, 4);
  private final static Scalar P1_4 = RationalScalar.of(+1, 4);
  private final static Scalar P5_4 = RationalScalar.of(+5, 4);
  private final static Scalar P3_4 = RationalScalar.of(+3, 4);
  // ---
  protected final SplitInterface splitInterface;
  private final Scalar lambda;
  private final Scalar _1_lam;

  public FourPointCurveSubdivision(SplitInterface splitInterface, Scalar omega) {
    super(splitInterface);
    this.splitInterface = splitInterface;
    Scalar two_omega = omega.add(omega);
    _1_lam = two_omega.negate();
    lambda = two_omega.add(RealScalar.ONE);
  }

  /** standard four point scheme with omega = 1/16
   * 
   * @param splitInterface */
  public FourPointCurveSubdivision(SplitInterface splitInterface) {
    this(splitInterface, P1_16);
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(2 * length);
    if (0 < length) {
      Tensor p = Last.of(tensor);
      Tensor q = tensor.get(0);
      Tensor r = tensor.get(1 % length);
      for (int index = 0; index < length; ++index) {
        list.add(q);
        list.add(center(p, p = q, q = r, r = tensor.get((index + 2) % length)));
      }
    }
    Integers.requireEquals(list.size(), 2 * length);
    return Unprotect.using(list);
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    int length = tensor.length();
    if (length < 3)
      return new BSpline3CurveSubdivision(splitInterface).string(tensor);
    // ---
    int capacity = 2 * length - 1;
    List<Tensor> list = new ArrayList<>(capacity);
    Tensor p = tensor.get(0);
    Tensor q = tensor.get(1);
    Tensor r = tensor.get(2);
    list.add(p);
    list.add(triple_lo(p, q, r));
    for (int index = 3; index < length; ++index) {
      list.add(q);
      list.add(center(p, p = q, q = r, r = tensor.get(index)));
    }
    list.add(q);
    list.add(triple_hi(p, q, r));
    list.add(r);
    Integers.requireEquals(list.size(), capacity);
    return Unprotect.using(list);
  }

  /** @param p
   * @param q
   * @param r
   * @param s
   * @return point between q and r */
  Tensor center(Tensor p, Tensor q, Tensor r, Tensor s) {
    return midpoint( //
        splitInterface.split(p, q, lambda), //
        splitInterface.split(r, s, _1_lam));
  }

  /** @param p
   * @param q
   * @param r
   * @return point between p and q */
  Tensor triple_lo(Tensor p, Tensor q, Tensor r) {
    return midpoint( //
        splitInterface.split(p, q, P1_4), //
        splitInterface.split(q, r, N1_4));
  }

  /** @param p
   * @param q
   * @param r
   * @return point between q and r */
  Tensor triple_hi(Tensor p, Tensor q, Tensor r) {
    return midpoint( //
        splitInterface.split(p, q, P5_4), //
        splitInterface.split(q, r, P3_4));
  }
}
