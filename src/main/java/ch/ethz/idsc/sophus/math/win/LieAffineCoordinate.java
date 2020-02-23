// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Lie affine coordinates are generalized barycentric coordinates for
 * scattered sets of points on a Lie-group with the properties:
 * 
 * coordinates sum up to 1
 * linear reproduction
 * Biinvariant: invariant under left-, right- and inverse action
 * 
 * However, generally NOT fulfilled:
 * Lagrange property
 * non-negativity
 * 
 * Log[g.m.g^-1] == Ad[g].Log[m]
 * Log[g.m] == Ad[g].Log[m.g]
 * Log[g^-1.m] == Ad[g^-1].Log[m.g^-1]
 * Ad[g].Log[g^-1.m] == Log[m.g^-1] */
public class LieAffineCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;

  /** @param lieGroup
   * @param log */
  public LieAffineCoordinate(LieGroup lieGroup, TensorUnaryOperator log) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor m) {
    Tensor levers = Tensor.of(sequence.stream() //
        .map(lieGroup.element(m).inverse()::combine) //
        .map(log));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = ConstantArray.of(RealScalar.ONE, sequence.length());
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}
