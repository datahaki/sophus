// code by jph
package ch.ethz.idsc.sophus.gbc.amp;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.tensor.DeterminateScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.MeanDeviation;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Sign;

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
  }, //
  EXP_ADAPT {
    @Override
    public TensorUnaryOperator supply(Scalar sigma) {
      ScalarUnaryOperator suo = new ArcTanAmplifier(sigma);
      TensorUnaryOperator tuo = Normalize.with(Norm._2);
      return errors -> {
        Scalar var = MeanDeviation.ofVector(errors);
        Tensor temp = errors.divide(var);
        // tuo.apply(errors);
        // System.out.println(temp);
        boolean allMatch = temp.stream().map(Scalar.class::cast).allMatch(DeterminateScalarQ::of);
        return allMatch ? temp : ConstantArray.of(RealScalar.ONE, errors.length());
      };
    }
  },;

  public abstract TensorUnaryOperator supply(Scalar sigma);

  public TensorUnaryOperator supply(Number sigma) {
    return supply(RealScalar.of(sigma));
  }
}
