// code by jph
package ch.alpine.sophus.math.sample;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Ceiling;
import ch.alpine.tensor.sca.Floor;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Sqrt;

public class PoissonDiskSampling {
  private final Scalar width;
  private final Scalar height;
  private final Scalar r;
  private final int k;
  private final RingRandomSample ringRandomSample;

  public PoissonDiskSampling(Scalar width, Scalar height, Scalar r, int k) {
    this.width = width;
    this.height = height;
    this.r = r;
    this.k = k;
    ringRandomSample = new RingRandomSample(2, r, r.add(r));
  }

  public List<Tensor> generate() {
    List<Tensor> points = new ArrayList<>();
    List<Tensor> active = new ArrayList<>();
    // Step 0: Initialize the background grid
    Scalar cellSize = r.divide(Sqrt.FUNCTION.apply(RealScalar.TWO)); // TODO generalize
    int cols = Ceiling.intValueExact(width.divide(cellSize));
    int rows = Ceiling.intValueExact(height.divide(cellSize));
    Tensor[][] grid = new Tensor[cols][rows];
    // Step 1: Select initial sample
    Tensor firstPoint = Times.of(Tensors.vector(Math.random(), Math.random()), Tensors.of(width, height));
    addPoint(firstPoint, points, active, grid, cellSize);
    // Step 2: Process active list
    while (!active.isEmpty()) {
      int randomIndex = (int) (Math.random() * active.size());
      Tensor p = active.get(randomIndex);
      boolean found = false;
      for (int i = 0; i < k; i++) {
        Tensor candidate = RandomSample.of(ringRandomSample).add(p);
        if (isValid(candidate, width, height, r, grid, cellSize, cols, rows)) {
          addPoint(candidate, points, active, grid, cellSize);
          found = true;
          break;
        }
      }
      if (!found) {
        active.remove(randomIndex);
      }
    }
    return points;
  }

  private static boolean isValid(Tensor p, Scalar w, Scalar h, Scalar r, Tensor[][] grid, Scalar cellSize, int cols, int rows) {
    if (Sign.isNegative(p.Get(0)) || Sign.isNegative(p.Get(1)) || Scalars.lessEquals(w, p.Get(0)) || Scalars.lessEquals(h, p.Get(1)))
      return false;
    int col = Floor.intValueExact(p.Get(0).divide(cellSize));
    int row = Floor.intValueExact(p.Get(1).divide(cellSize));
    // Check neighboring 5x5 cells in the grid
    for (int i = Math.max(0, col - 2); i <= Math.min(cols - 1, col + 2); i++) {
      for (int j = Math.max(0, row - 2); j <= Math.min(rows - 1, row + 2); j++) {
        Tensor neighbor = grid[i][j];
        if (neighbor != null) {
          Scalar d = Vector2Norm.between(p, neighbor);
          if (Scalars.lessThan(d, r))
            return false;
        }
      }
    }
    return true;
  }

  private static void addPoint(Tensor p, List<Tensor> points, List<Tensor> active, Tensor[][] grid, Scalar cellSize) {
    points.add(p);
    active.add(p);
    grid[Floor.intValueExact(p.Get(0).divide(cellSize))][Floor.intValueExact(p.Get(1).divide(cellSize))] = p;
  }
}
