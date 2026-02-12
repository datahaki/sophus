// code by jph
package ch.alpine.sophus.lie.se2;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.sophus.lie.se.SeNGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;
import ch.alpine.tensor.sca.tri.Tan;

/** Hint:
 * The angular coordinate is not automatically mapped to [-pi, pi).
 *
 * References:
 * <a href="http://vixra.org/abs/1807.0463">1807.0463</a>
 * <a href="https://www.youtube.com/watch?v=2vDciaUgL4E">video</a> */
public class Se2CoveringGroup implements LieGroup, MatrixGroup, VectorEncodingMarker, Serializable {
  private static final Scalar HALF = RealScalar.of(0.5);
  public static final Se2CoveringGroup INSTANCE = new Se2CoveringGroup();
  // ---

  protected Se2CoveringGroup() {
    // ---
  }

  @Override
  public final MemberQ isPointQ() {
    return t -> VectorQ.ofLength(t, 3);
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return new Se2BiinvariantMean(this, LinearBiinvariantMean.INSTANCE);
  }

  private class Exponential0 implements Exponential {
    /** maps a vector x from the Lie-algebra se2 to a vector of the Lie-group SE2
     * 
     * @param x element in the se2 Lie-algebra of the form {vx, vy, beta}
     * @return element g in SE2 as vector with coordinates of g == exp x */
    @Override // from Exponential
    public Tensor exp(Tensor x) {
      Scalar be = x.Get(2);
      if (Scalars.isZero(be))
        return x.copy();
      Scalar vx = x.Get(0);
      Scalar vy = x.Get(1);
      Scalar cd = Cos.FUNCTION.apply(be).subtract(RealScalar.ONE);
      Scalar sd = Sin.FUNCTION.apply(be);
      return Tensors.of( //
          sd.multiply(vx).add(cd.multiply(vy)).divide(be), //
          sd.multiply(vy).subtract(cd.multiply(vx)).divide(be), //
          truncate(be));
    }

    /** @param g element in the SE2 Lie group of the form {px, py, beta}
     * @return element x in the se2 Lie algebra with x == log g, and g == exp x */
    @Override // from Exponential
    public Tensor log(Tensor g) {
      Scalar be = g.Get(2);
      Scalar be2 = be.multiply(HALF);
      Scalar tan = Tan.FUNCTION.apply(be2);
      if (Scalars.isZero(tan))
        return g.copy();
      Scalar x = g.Get(0);
      Scalar y = g.Get(1);
      return Tensors.of( //
          y.add(x.divide(tan)).multiply(be2), //
          y.divide(tan).subtract(x).multiply(be2), //
          be);
    }

    @Override
    public ZeroDefectArrayQ isTangentQ() {
      return VectorQ.ofLength(3);
    }
  }

  @Override
  public final Exponential exponential0() {
    return new Exponential0();
  }

  @Override
  public final Tensor neutral(Tensor element) {
    return element.maps(Scalar::zero);
  }

  @Override
  public final Tensor invert(Tensor xya) {
    Scalar px = xya.Get(0);
    Scalar py = xya.Get(1);
    Scalar pa = xya.Get(2);
    Scalar ca = Cos.FUNCTION.apply(pa);
    Scalar sa = Sin.FUNCTION.apply(pa);
    return Tensors.of( //
        px.multiply(ca).add(py.multiply(sa)).negate(), //
        px.multiply(sa).subtract(py.multiply(ca)), //
        pa.negate());
  }

  protected Scalar truncate(Scalar a) {
    return a;
  }

  @Override
  public final Tensor combine(Tensor xya, Tensor tensor) {
    Scalar px = xya.Get(0);
    Scalar py = xya.Get(1);
    Scalar pa = xya.Get(2);
    Scalar ca = Cos.FUNCTION.apply(pa);
    Scalar sa = Sin.FUNCTION.apply(pa);
    Scalar qx = tensor.Get(0);
    Scalar qy = tensor.Get(1);
    Scalar qa = tensor.Get(2);
    return Tensors.of( //
        px.add(qx.multiply(ca)).subtract(qy.multiply(sa)), //
        py.add(qy.multiply(ca)).add(qx.multiply(sa)), //
        truncate(pa.add(qa)));
  }

  @Override // from LieGroupElement
  public final Tensor adjoint(Tensor xya, Tensor uvw) {
    Scalar px = xya.Get(0);
    Scalar py = xya.Get(1);
    Scalar pa = xya.Get(2);
    Scalar ca = Cos.FUNCTION.apply(pa);
    Scalar sa = Sin.FUNCTION.apply(pa);
    Scalar u = uvw.Get(0);
    Scalar v = uvw.Get(1);
    Scalar w = uvw.Get(2);
    return Tensors.of( //
        ca.multiply(u).subtract(sa.multiply(v)).add(py.multiply(w)), //
        sa.multiply(u).add(ca.multiply(v)).subtract(px.multiply(w)), //
        w);
  }

  @Override // from LieGroupElement
  public final Tensor dL(Tensor xya, Tensor uvw) {
    Scalar pa = xya.Get(2);
    Scalar ca = Cos.FUNCTION.apply(pa);
    Scalar sa = Sin.FUNCTION.apply(pa);
    Scalar u = uvw.Get(0);
    Scalar v = uvw.Get(1);
    Scalar w = uvw.Get(2);
    return Tensors.of( //
        ca.multiply(u).add(sa.multiply(v)), //
        ca.multiply(v).subtract(sa.multiply(u)), //
        w);
  }

  @Override // from LieGroupElement
  public final Tensor dR(Tensor xya, Tensor uvw) {
    Scalar px = xya.Get(0);
    Scalar py = xya.Get(1);
    Scalar u = uvw.Get(0);
    Scalar v = uvw.Get(1);
    Scalar w = uvw.Get(2);
    return Tensors.of( //
        u.subtract(py.multiply(w)), //
        v.add(px.multiply(w)), //
        w);
  }

  @Override
  public final Tensor matrixBasis() {
    return new SeNGroup(2).matrixBasis();
  }

  @Override
  public final int dimensions() {
    return 3;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SE~", 2);
  }
}
