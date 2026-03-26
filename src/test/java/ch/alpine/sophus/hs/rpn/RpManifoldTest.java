package ch.alpine.sophus.hs.rpn;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.VectorAngle;
import ch.alpine.tensor.num.Pi;

class RpManifoldTest {
  @Test
  void test() {
    RpManifold rpManifold = RpManifold.INSTANCE;
    Tensor p = Tensors.vector(1, 0, 0);
    Tensor q = Tensors.vector(-1, 0, 0);
    Scalar distance = rpManifold.distance(p, q);
    Optional<Scalar> optional = VectorAngle.of(p, q);
    Tolerance.CHOP.requireZero(distance);
    Tolerance.CHOP.requireClose(optional.orElseThrow(), Pi.VALUE);
  }
}
