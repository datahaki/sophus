// code by jph
package ch.alpine.sophus.gbc.it;

import java.util.Deque;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.Tensor;

public interface GenesisDeque extends Genesis {
  Deque<WeightsFactors> deque(Tensor levers);
}
