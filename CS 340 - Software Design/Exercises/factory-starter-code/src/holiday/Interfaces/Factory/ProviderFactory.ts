import { TableclothPatternProvider } from "../Provider/TableClothPatternProvider";
import { WallHangingProvider } from "../Provider/WallHangingProvider";
import { YardOrnamentProvider } from "../Provider/YardOrnamentProvider";

export interface ProviderFactory {

  tableClothPatternProvider: TableclothPatternProvider;
  wallHangingProvider: WallHangingProvider;
  yardOrnamentProvider: YardOrnamentProvider;
}