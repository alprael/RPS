package edu.cnm.deepdive.rps.model;

import java.util.Random;

public class Terrain {

  // Gris is assumed to be square.
  private RpsBreed[][] grid;
  private Random rng;
  private Neighborhood neighborhood;

  public Terrain(int size, Random rng, Neighborhood neighborhood) {
    grid = new RpsBreed[size][size];
    this.rng = rng;
    this.neighborhood = neighborhood;
  }

  public void reset() {
    for (RpsBreed[] row : grid) {
      for (int col = 0; col < row.length; col++) {
        row[col] = RpsBreed.values()[rng.nextInt(RpsBreed.values().length)];
      }
    }
  }
//    for (int row = 0; row < grid.length; row++) {
//      RpsBreed[] rowContents = grid[row];
//      for (int col = 0; col < rowContents.length; col++) {
//        rowContents[col] = RpsBreed.values()[rng.nextInt(RpsBreed.values().length)];
//      }
//    }
//  }

  public void step() {
    Location attackLocation = new Location(rng.nextInt(grid.length), rng.nextInt(grid.length));
    RpsBreed attacker = grid[attackLocation.getRow()][attackLocation.getColumn()];
    Location offset = neighborhood.randomNeighbor(rng);
    Location defenderLocation = normalize(attackLocation, offset);
    RpsBreed defender = grid[defenderLocation.getRow()][defenderLocation.getColumn()];
    int result = RpsBreed.REFEREE.compare(attacker, defender);
    if (result < 0) {
      grid[attackLocation.getRow()][attackLocation.getColumn()] = defender;
    } else if (result > 0) {
      grid[defenderLocation.getRow()][defenderLocation.getColumn()] = attacker;
    }
  }

  public void step(int numSteps) {
    for (int i = 0; i < numSteps; i++) {
      step();
    }
  }

  /**
   * Returns a reference to the terrain contents. <strong>Important</strong>
   * This is <strong>not</strong> a safe copy.
   *
   * @return
   */
  public RpsBreed[][] getGrid() {
    return grid;
  }

  private Location normalize(Location base, Location offset) {
    int row = (base.getRow() + offset.getRow() + grid.length) % grid.length;
    int col = (base.getColumn() + offset.getColumn() + grid.length) % grid.length;
    return new Location(row, col);
  }
}
