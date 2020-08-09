// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.15
 * 
 * @see PoleLadder
 * @see SchildLadder */
public class SubdivideTransport implements HsTransport, Serializable {
  /** @param hsTransport
   * @param hsExponential
   * @param n
   * @return */
  public static HsTransport of(HsTransport hsTransport, HsExponential hsExponential, int n) {
    return new SubdivideTransport( //
        Objects.requireNonNull(hsTransport), hsExponential, n);
  }

  /***************************************************/
  private final HsTransport hsTransport;
  private final HsGeodesic hsGeodesic;
  private final Tensor domain;
  private final Scalar factor;

  public SubdivideTransport(HsTransport hsTransport, HsExponential hsExponential, int n) {
    this.hsTransport = hsTransport;
    hsGeodesic = new HsGeodesic(hsExponential);
    domain = Subdivide.of(0, 1, n);
    this.factor = RealScalar.of(n);
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new Rung(xo, xw);
  }

  private class Rung implements TensorUnaryOperator {
    private final Tensor points;

    private Rung(Tensor xo, Tensor xw) {
      points = domain.map(hsGeodesic.curve(xo, xw));
    }

    @Override
    public Tensor apply(Tensor vo) {
      vo = vo.divide(factor);
      Iterator<Tensor> iterator = points.iterator();
      Tensor prev = iterator.next();
      while (iterator.hasNext())
        vo = hsTransport.shift(prev, prev = iterator.next()).apply(vo);
      return vo.multiply(factor);
    }
  }
}
