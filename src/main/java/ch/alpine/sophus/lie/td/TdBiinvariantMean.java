// code by jph
package ch.alpine.sophus.lie.td;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.lie.sc.ScGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.exp.Logc;

public enum TdBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    int n = Unprotect.dimension1Hint(sequence) - 1;
    Tensor lambdas = sequence.get(Tensor.ALL, n);
    // Reference 1, p.27 "weighted geometric mean of scalings"
    Scalar scmean = ScGroup.INSTANCE.biinvariantMean().mean(lambdas.map(Tensors::of), weights).Get(0);
    Tensor alphaw = NormalizeTotal.FUNCTION.apply(Times.of(weights, lambdas.divide(scmean).map(Logc.FUNCTION)));
    Tensor tn_seq = Tensor.of(sequence.stream().map(row -> Drop.tail(row, 1)));
    Tensor trmean = LinearBiinvariantMean.INSTANCE.mean(tn_seq, alphaw);
    return trmean.append(scmean); // "scalings reweighted arithmetic mean of translations"
  }
}
