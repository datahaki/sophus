// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.lie.HodgeDual;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.pow.Sqrt;
import ch.alpine.tensor.sca.tri.Cosh;
import ch.alpine.tensor.sca.tri.Sinhc;

/** References:
 * "Lie Group methods", p. 313, 334
 * by Arieh Iserles, Hans Z. Munthe-Kaas, Syvert P. Nørsett, Antonella Zanna
 * 
 * Geometry VI Riemannian Geometry
 * Chapter 12. Metric Properties of Geodesics, p.158
 * by M. M. Postnikov */
public enum Sl2Group implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public Sl2GroupElement element(Tensor vector) {
    return Sl2GroupElement.create(vector);
  }

  private static final Tensor ID_NEGATE = IdentityMatrix.of(2).negate();
  private static final Tensor LOG_ID_NEGATE = HodgeDual.of(Pi.VALUE, 2);
  private static final Scalar TWO_NEGATE = RealScalar.of(-2);

  @Override // from Exponential
  public Tensor exp(Tensor x) {
    TSlMemberQ.INSTANCE.require(x);
    // x = {{a, b}, {c, -a}}
    Scalar a = x.Get(0, 0);
    Scalar b = x.Get(0, 1);
    Scalar c = x.Get(1, 0);
    Scalar w = Sqrt.FUNCTION.apply(a.multiply(a).add(b.multiply(c)));
    return DiagonalMatrix.of(2, Cosh.FUNCTION.apply(w)).add(x.multiply(Sinhc.FUNCTION.apply(w)));
  }

  @Override // from Exponential
  public Tensor log(Tensor g) {
    SlMemberQ.INSTANCE.require(g);
    if (Tolerance.CHOP.isClose(g, ID_NEGATE))
      return LOG_ID_NEGATE;
    Tensor log = MatrixLog.of(g);
    if (Scalars.lessEquals(Trace.of(g), TWO_NEGATE))
      throw new Throw(g);
    // TODO SOPHUS ALG move check to test area and remove here
    Chop._04.requireClose(g, exp(log));
    return log;
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor q) {
    // specific to Sl2, skip 1, or limit 3
    return Tensor.of(log(q).flatten(1).skip(1));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.reduce(this, chop);
  }
}
