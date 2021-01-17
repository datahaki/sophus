// code by jph
package ch.ethz.idsc.sophus.clt;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import ch.ethz.idsc.sophus.clt.mid.ClothoidQuadratic;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegration;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegrations;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

public enum ClothoidEmit {
  ;
  public static Stream<Clothoid> stream(ClothoidContext clothoidContext, Tensor lambdas) {
    Builder<Clothoid> builder = Stream.builder();
    for (Tensor _lambda : lambdas) {
      ClothoidQuadratic clothoidQuadratic = CustomClothoidQuadratic.of((Scalar) _lambda);
      ClothoidIntegration clothoidIntegration = ClothoidIntegrations.ANALYTIC;
      ClothoidBuilderImpl clothoidBuilderImpl = new ClothoidBuilderImpl(clothoidQuadratic, clothoidIntegration);
      builder.accept(clothoidBuilderImpl.from(clothoidContext));
    }
    return builder.build();
  }
}
