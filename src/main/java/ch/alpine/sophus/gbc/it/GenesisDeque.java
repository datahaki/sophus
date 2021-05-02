// code by jph
package ch.alpine.sophus.gbc.it;

import java.util.Deque;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;

public interface GenesisDeque extends Genesis {
  Deque<Evaluation> deque(Tensor levers);
}
