// code by jph
package ch.alpine.sophus.lie.su;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.ad.BakerCampbellHausdorff;
import ch.alpine.sophus.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Sqrt;

/** Algebra
 * Hermitian and Tr == 0
 * 
 * Reference:
 * "Lie Algebras in Particle Physics From Isospin to Unified Theories", 1999
 * 
 * https://en.wikipedia.org/wiki/Quantum_chromodynamics */
public enum Su3Algebra implements LieAlgebra {
  INSTANCE;

  private final Scalar SQRT3 = Sqrt.FUNCTION.apply(RationalScalar.of(1, 3));
  private final Tensor ad;

  private Su3Algebra() {
    ad = new MatrixAlgebra(basis()).ad();
  }

  @Override // from LieAlgebra
  public Tensor ad() {
    return ad;
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad(), degree);
  }

  @Override // from LieAlgebra
  public Tensor basis() {
    // https://en.wikipedia.org/wiki/Gell-Mann_matrices
    Tensor u0 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor u1 = Tensors.fromString("{{0, -I, 0}, {I, 0, 0}, {0, 0, 0}}");
    Tensor u2 = Tensors.fromString("{{1, 0, 0}, {0, -1, 0}, {0, 0, 0}}");
    Tensor u3 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Tensor u4 = Tensors.fromString("{{0, 0, -I}, {0, 0, 0}, {I, 0, 0}}");
    Tensor u5 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}");
    Tensor u6 = Tensors.fromString("{{0, 0, 0}, {0, 0, -I}, {0, I, 0}}");
    Tensor u7 = Tensors.fromString("{{1, 0, 0}, {0, 1, 0}, {0, 0, -2}}").multiply(SQRT3);
    return Tensors.of(u0, u1, u2, u3, u4, u5, u6, u7).multiply(RationalScalar.HALF);
  }
}
