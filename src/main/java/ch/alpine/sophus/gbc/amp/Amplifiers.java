// code by jph
package ch.alpine.sophus.gbc.amp;

import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.FiniteTensorQ;
import ch.alpine.tensor.red.MeanDeviation;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;

/** Properties:
 * strictly positive
 * monotonous
 * smooth
 * 0 maps to 1
 * 
 * @param sigma
 * @return */
public enum Amplifiers {
  EXP {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      Sign.requirePositive(sigma);
      ScalarUnaryOperator suo = scalar -> Exp.FUNCTION.apply(scalar.multiply(sigma));
      return weights -> {
        Tensor average = AveragingWeights.INSTANCE.origin(weights);
        return weights.subtract(average).map(suo);
      };
    }
  },
  EXP_ZERO {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      Sign.requirePositive(sigma);
      ScalarUnaryOperator suo = scalar -> Exp.FUNCTION.apply(Min.of(scalar, scalar.zero()).multiply(sigma));
      return weights -> {
        return weights.map(suo);
      };
    }
  },
  RAMP {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      ScalarUnaryOperator suo = new SmoothRamp(sigma);
      return weights -> {
        Tensor average = AveragingWeights.INSTANCE.origin(weights);
        return weights.subtract(average).map(suo);
      };
    }
  },
  ARCTAN {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      ScalarUnaryOperator suo = new ArcTanAmplifier(sigma);
      return weights -> {
        Tensor average = AveragingWeights.INSTANCE.origin(weights);
        return weights.subtract(average).map(suo);
      };
    }
  },
  EXP_ADAPT {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      // ScalarUnaryOperator suo = new ArcTanAmplifier(sigma);
      // TensorUnaryOperator tuo = Normalize.with(Norm._2);
      return errors -> {
        Scalar var = MeanDeviation.ofVector(errors);
        Tensor temp = errors.divide(var);
        // tuo.apply(errors);
        // System.out.println(temp);
        return FiniteTensorQ.of(temp) ? temp : ConstantArray.of(RealScalar.ONE, errors.length());
      };
    }
  },;

  public abstract TensorUnaryOperator supply(Scalar sigma);

  public TensorUnaryOperator supply(Number sigma) {
    return supply(RealScalar.of(sigma));
  }
}
