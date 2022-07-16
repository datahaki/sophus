// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

public class HermiteLoParam {
  private static final Scalar N1_8 = RationalScalar.of(-1, 8);
  private static final Scalar N1_2 = RationalScalar.of(-1, 2);
  /* package */ static final HermiteLoParam STANDARD = new HermiteLoParam(N1_8, N1_2);
  // ---
  private static final Scalar N1_5 = RationalScalar.of(-1, 5);
  private static final Scalar _9_10 = RationalScalar.of(9, 10);
  /* package */ static final HermiteLoParam MANIFOLD = new HermiteLoParam(N1_5, _9_10);
  // ---
  public Scalar lambda;
  public Scalar mu;

  public HermiteLoParam(Scalar lambda, Scalar mu) {
    this.lambda = lambda;
    this.mu = mu;
  }
}
