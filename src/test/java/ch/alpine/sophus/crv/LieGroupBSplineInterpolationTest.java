// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.AbstractBSplineInterpolation.Iteration;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.BernoulliDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

class LieGroupBSplineInterpolationTest {
  @Test
  void testIterationRnExact() {
    Tensor target = Tensors.vector(1, 2, 0, 2, 1, 3);
    LieGroupBSplineInterpolation lieGroupBSplineInterpolation = //
        new LieGroupBSplineInterpolation(RnGroup.INSTANCE, 2, target);
    Iteration it0 = lieGroupBSplineInterpolation.init();
    Iteration it1 = it0.stepGaussSeidel();
    ExactTensorQ.require(it1.control());
  }

  @Test
  void testApplyRn() {
    Tensor target = Tensors.vector(1, 2, 0, 2, 1, 3).map(N.DOUBLE);
    LieGroupBSplineInterpolation lieGroupBSplineInterpolation = //
        new LieGroupBSplineInterpolation(RnGroup.INSTANCE, 2, target);
    Tensor control = lieGroupBSplineInterpolation.apply();
    Tensor vector = Tensors.vector(1, 2.7510513036161504, -0.922624053826282, 2.784693019343523, 0.21446593776315992, 3);
    Chop._10.requireClose(control, vector);
  }

  @Test
  void testApplyRnExact() {
    Tensor target = Tensors.vector(1, 2, 0, 2, 1, 3);
    LieGroupBSplineInterpolation lieGroupBSplineInterpolation = //
        new LieGroupBSplineInterpolation(RnGroup.INSTANCE, 2, target);
    Tensor control = lieGroupBSplineInterpolation.apply();
    Tensor vector = Tensors.vector(1, 2.7510513036161504, -0.922624053826282, 2.784693019343523, 0.21446593776315992, 3);
    Chop._10.requireClose(control, vector);
  }

  @Test
  void testExactRnConvergence() {
    Tensor target = RandomVariate.of(BernoulliDistribution.of(RationalScalar.HALF), 10, 3);
    LieGroupBSplineInterpolation lieGroupBSplineInterpolation = //
        new LieGroupBSplineInterpolation(RnGroup.INSTANCE, 3, target);
    lieGroupBSplineInterpolation.apply();
  }

  @Test
  void testIterationRnConvergence() {
    Tensor target = Tensors.vector(1, 2, 0, 2, 1, 3).map(N.DOUBLE);
    LieGroupBSplineInterpolation lieGroupBSplineInterpolation = //
        new LieGroupBSplineInterpolation(RnGroup.INSTANCE, 3, target);
    Iteration iteration = lieGroupBSplineInterpolation.init();
    Tensor p = iteration.control();
    for (int index = 0; index < 100; ++index) {
      iteration = iteration.stepGaussSeidel();
      Tensor q = iteration.control();
      if (Scalars.isZero(Vector2Norm.between(p, q)))
        return;
      p = q;
    }
    fail();
  }
}
