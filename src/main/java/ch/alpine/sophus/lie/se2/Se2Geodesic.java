//// code by jph
// package ch.alpine.sophus.lie.se2;
//
// import ch.alpine.sophus.api.GeodesicSpace;
// import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
// import ch.alpine.tensor.Tensor;
// import ch.alpine.tensor.api.ScalarTensorFunction;
//
//
// public enum Se2Geodesic implements GeodesicSpace {
// INSTANCE;
//
// @Override // from GeodesicInterface
// public ScalarTensorFunction curve(Tensor p, Tensor q) {
// Tensor delta = new Se2GroupElement(p).inverse().combine(q);
// Tensor x = Se2CoveringExponential.INSTANCE.log(delta);
// return scalar -> Se2Integrator.INSTANCE.spin(p, x.multiply(scalar));
// }
// }
