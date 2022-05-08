// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** References:
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.15
 * 
 * @see PoleLadder
 * @see SchildLadder */
public class SubdivideTransport implements HsTransport, Serializable {
  /** @param hsTransport
   * @param geodesicSpace
   * @param n
   * @return */
  public static HsTransport of(HsTransport hsTransport, GeodesicSpace geodesicSpace, int n) {
    return new SubdivideTransport( //
        Objects.requireNonNull(hsTransport), //
        Objects.requireNonNull(geodesicSpace), //
        n);
  }

  // ---
  private final HsTransport hsTransport;
  private final GeodesicSpace geodesicSpace;
  private final Tensor domain;
  private final Scalar factor;

  public SubdivideTransport(HsTransport hsTransport, GeodesicSpace geodesicSpace, int n) {
    this.hsTransport = hsTransport;
    this.geodesicSpace = geodesicSpace;
    domain = Subdivide.of(0, 1, n);
    this.factor = RealScalar.of(n);
  }

  @Override
  public TensorUnaryOperator shift(Tensor xo, Tensor xw) {
    return new Rung(domain.map(geodesicSpace.curve(xo, xw)));
  }

  private class Rung implements TensorUnaryOperator {
    private final Tensor points;

    private Rung(Tensor points) {
      this.points = points;
    }

    @Override
    public Tensor apply(Tensor vo) {
      vo = vo.divide(factor);
      Iterator<Tensor> iterator = points.iterator();
      for (Tensor prev = iterator.next(); iterator.hasNext();)
        vo = hsTransport.shift(prev, prev = iterator.next()).apply(vo);
      return vo.multiply(factor);
    }
  }
}
