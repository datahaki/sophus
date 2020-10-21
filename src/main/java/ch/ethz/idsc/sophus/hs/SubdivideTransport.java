// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.15
 * 
 * @see PoleLadder
 * @see SchildLadder */
public class SubdivideTransport implements HsTransport, Serializable {
  private static final long serialVersionUID = -2577380661194204229L;

  /** @param hsTransport
   * @param geodesicInterface
   * @param n
   * @return */
  public static HsTransport of(HsTransport hsTransport, GeodesicInterface geodesicInterface, int n) {
    return new SubdivideTransport( //
        Objects.requireNonNull(hsTransport), //
        Objects.requireNonNull(geodesicInterface), //
        n);
  }

  /***************************************************/
  private final HsTransport hsTransport;
  private final GeodesicInterface geodesicInterface;
  private final Tensor domain;
  private final Scalar factor;

  public SubdivideTransport(HsTransport hsTransport, GeodesicInterface geodesicInterface, int n) {
    this.hsTransport = hsTransport;
    this.geodesicInterface = geodesicInterface;
    domain = Subdivide.of(0, 1, n);
    this.factor = RealScalar.of(n);
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new Rung(domain.map(geodesicInterface.curve(xo, xw)));
  }

  private class Rung implements TensorUnaryOperator {
    private static final long serialVersionUID = 4553566926090806166L;
    // ---
    private final Tensor points;

    private Rung(Tensor points) {
      this.points = points;
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
