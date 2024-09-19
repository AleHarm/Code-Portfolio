import { ProviderFactory } from "../../Interfaces/Factory/ProviderFactory";
import { HalloweenTableclothPatternProvider } from "../Provider/HalloweenTableclothPatternProvider";
import { HalloweenWallHangingProvider } from "../Provider/HalloweenWallHangingProvider";
import { HalloweenYardOrnamentProvider } from "../Provider/HalloweenYardOrnamentProvider";

export class HalloweenProviderFactory implements ProviderFactory{

  tableClothPatternProvider: HalloweenTableclothPatternProvider = new HalloweenTableclothPatternProvider();
  wallHangingProvider: HalloweenWallHangingProvider = new HalloweenWallHangingProvider();
  yardOrnamentProvider: HalloweenYardOrnamentProvider = new HalloweenYardOrnamentProvider();
}