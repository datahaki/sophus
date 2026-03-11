// code by jph
package ch.alpine.sophus.lie.td;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Expc;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.exp.Logc;

enum TdExponential implements LieExponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor dt_dlambda) {
    Tensor dt = Drop.tail(dt_dlambda, 1);
    Scalar dl = Last.of(dt_dlambda);
    return Append.of( //
        dt.multiply(Expc.FUNCTION.apply(dl)), //
        Exp.FUNCTION.apply(dl));
  }

  @Override // from Exponential
  public Tensor log(Tensor t_lambda) {
    Scalar lambda = Sign.requirePositive(Last.of(t_lambda));
    Scalar log_l = Log.FUNCTION.apply(lambda);
    return Append.of( //
        /* there is a typo in Reference 1 (!) */
        Drop.tail(t_lambda, 1).multiply(Logc.FUNCTION.apply(lambda)), //
        log_l);
  }

  @Override
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.INSTANCE;
  }

  @Override
  public Tensor gl_representation(Tensor dt_dlambda) {
    Tensor dt = Drop.tail(dt_dlambda, 1);
    int n = dt.length();
    Scalar dl = Last.of(dt_dlambda);
    return ArrayFlatten.of(new Tensor[][] { //
        { DiagonalMatrix.of(dt.length(), dl), Array.zeros(n, 1) }, //
        { Tensors.of(dt), Array.zeros(1, 1) } });
  }
}
