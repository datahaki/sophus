// code by jph
package ch.alpine.sophus.lie.su;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;

/** Algebra
 * Hermitian and Tr == 0
 * 
 * Reference:
 * "Lie Algebras in Particle Physics From Isospin to Unified Theories", 1999
 * 
 * https://en.wikipedia.org/wiki/Quantum_chromodynamics */
public enum Su3Algebra implements LieAlgebra {
  INSTANCE;

  private final Tensor ad;

  Su3Algebra() {
    ad = new MatrixAlgebra(Su3AlgebraBasis.basis()).ad();
  }

  @Override // from LieAlgebra
  public Tensor ad() {
    return ad;
  }

  @Override // from LieAlgebra
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad(), degree);
  }
}
