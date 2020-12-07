// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Deque;

import ch.ethz.idsc.sophus.gbc.IterativeAffineCoordinate.Evaluation;
import ch.ethz.idsc.tensor.Tensor;

public interface GenesisDeque extends Genesis {
  Deque<Evaluation> deque(Tensor levers);
}
