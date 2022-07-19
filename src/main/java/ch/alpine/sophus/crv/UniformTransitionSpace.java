// code by jph
package ch.alpine.sophus.crv;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;

public class UniformTransitionSpace implements TransitionSpace {
  private static final Tensor DOMAIN = Subdivide.of(0.0, 1.0, 10);
  // ---
  private final GeodesicSpace geodesicSpace;

  public UniformTransitionSpace(GeodesicSpace geodesicSpace) {
    this.geodesicSpace = geodesicSpace;
  }

  @Override
  public Transition connect(Tensor head, Tensor tail) {
    return new UniformTransition(head, tail);
  }

  public class UniformTransition implements Transition {
    private final Tensor head;
    private final Tensor tail;

    public UniformTransition(Tensor head, Tensor tail) {
      this.head = head;
      this.tail = tail;
    }

    @Override
    public Tensor start() {
      return head;
    }

    @Override
    public Tensor end() {
      return tail;
    }

    @Override
    public Scalar length() {
      return null;
    }

    @Override
    public Tensor sampled(Scalar minResolution) {
      ScalarTensorFunction curve = geodesicSpace.curve(head, tail);
      return DOMAIN.map(curve);
    }

    @Override
    public Tensor linearized(Scalar minResolution) {
      ScalarTensorFunction curve = geodesicSpace.curve(head, tail);
      return DOMAIN.map(curve);
    }

    @Override
    public TransitionWrap wrapped(Scalar minResolution) {
      throw new UnsupportedOperationException();
    }
  }
}
