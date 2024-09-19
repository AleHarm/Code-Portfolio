import { DecorationPlacer } from "./decoration/DecorationPlacer";
import { ChristmasProviderFactory } from "./holiday/Christmas/Factory/ChristmasProviderFactory";
import { HalloweenProviderFactory } from "./holiday/Halloween/Factory/HalloweenProviderFactory"

main();

function main(): void {

  let halloweenFactory = new HalloweenProviderFactory();
  let decorationPlacer = new DecorationPlacer(halloweenFactory);

  console.log(decorationPlacer.placeDecorations());

  let christmasFactory = new ChristmasProviderFactory();
  decorationPlacer = new DecorationPlacer(christmasFactory);

  console.log(decorationPlacer.placeDecorations());
}
