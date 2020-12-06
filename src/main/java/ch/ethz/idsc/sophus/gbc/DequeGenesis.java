package ch.ethz.idsc.sophus.gbc;

import java.util.Deque;

import ch.ethz.idsc.sophus.gbc.IterativeAffineCoordinate.Evaluation;
import ch.ethz.idsc.tensor.Tensor;

public interface DequeGenesis extends Genesis {
  Deque<Evaluation> factors(Tensor levers);
}
