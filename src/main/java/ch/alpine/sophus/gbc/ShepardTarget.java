package ch.alpine.sophus.gbc;

// package ch.ethz.idsc.sophus.gbc;
//
// import ch.ethz.idsc.sophus.krg.InverseDistanceWeighting;
// import ch.ethz.idsc.sophus.math.NormalizeTotal;
// import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
// import ch.ethz.idsc.tensor.RealScalar;
// import ch.ethz.idsc.tensor.Scalar;
// import ch.ethz.idsc.tensor.Tensor;
// import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
// import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
// import ch.ethz.idsc.tensor.sca.Exp;
//
// public class ShepardTarget implements TensorUnaryOperator {
// private final Tensor target;
//
// public ShepardTarget(Tensor levers) {
// Genesis genesis = InverseDistanceWeighting.of(InversePowerVariogram.of(2));
// genesis = MetricCoordinate.of(InversePowerVariogram.of(2));
// target = genesis.origin(levers);
// }
//
// @Override
// public Tensor apply(Tensor weights) {
// Scalar sigma = RealScalar.of(1);
// ScalarUnaryOperator suo = scalar -> Exp.FUNCTION.apply(scalar.multiply(sigma));
// return NormalizeTotal.FUNCTION.apply(weights.subtract(target).map(suo));
// }
// }
