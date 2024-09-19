import { TableclothPatternProvider } from "../holiday/Interfaces/Provider/TableClothPatternProvider"
import { WallHangingProvider } from "../holiday/Interfaces/Provider/WallHangingProvider"
import { YardOrnamentProvider } from "../holiday/Interfaces/Provider/YardOrnamentProvider"
import { ProviderFactory } from "../holiday/Interfaces/Factory/ProviderFactory";


export class DecorationPlacer {
  private tableclothPattern: TableclothPatternProvider;
  private wallHanging: WallHangingProvider;
  private yardOrnament: YardOrnamentProvider;

  public constructor( factory: ProviderFactory){

    this.tableclothPattern = factory.tableClothPatternProvider;
    this.wallHanging = factory.wallHangingProvider;
    this.yardOrnament = factory.yardOrnamentProvider;
  }

  placeDecorations(): string {
    return (
      "Everything was ready for the party. The " +
      this.yardOrnament.getOrnament() +
      " was in front of the house, the " +
      this.wallHanging.getHanging() +
      " was hanging on the wall, and the tablecloth with " +
      this.tableclothPattern.getTablecloth() +
      " was spread over the table."
    );
  }
}
