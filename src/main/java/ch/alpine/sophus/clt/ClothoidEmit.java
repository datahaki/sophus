// code by jph
package ch.alpine.sophus.clt;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import ch.alpine.sophus.clt.mid.ClothoidQuadratic;
import ch.alpine.sophus.clt.par.ClothoidIntegration;
import ch.alpine.sophus.clt.par.ClothoidIntegrations;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

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
