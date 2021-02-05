// code by jph
package ch.ethz.idsc.sophus.gbc.it;

import java.util.Deque;

import ch.ethz.idsc.sophus.gbc.it.IterativeAffineCoordinate.Evaluation;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;

public interface GenesisDeque extends Genesis {
  Deque<Evaluation> deque(Tensor levers);
}
