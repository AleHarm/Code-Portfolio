import { ProviderFactory } from "../../Interfaces/Factory/ProviderFactory";
import { ChristmasTableclothPatternProvider } from "../Provider/ChristmasTableclothPatternProvider";
import { ChristmasWallHangingProvider } from "../Provider/ChristmasWallHangingProvider";
import { ChristmasYardOrnamentProvider } from "../Provider/ChristmasYardOrnamentProvider";

export class ChristmasProviderFactory implements ProviderFactory{

  tableClothPatternProvider: ChristmasTableclothPatternProvider = new ChristmasTableclothPatternProvider();
  wallHangingProvider: ChristmasWallHangingProvider = new ChristmasWallHangingProvider();
  yardOrnamentProvider: ChristmasYardOrnamentProvider = new ChristmasYardOrnamentProvider();
}