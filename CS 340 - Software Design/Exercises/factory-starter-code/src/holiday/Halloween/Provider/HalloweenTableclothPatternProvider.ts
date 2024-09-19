import { TableclothPatternProvider } from "../../Interfaces/Provider/TableClothPatternProvider";

export class HalloweenTableclothPatternProvider implements TableclothPatternProvider {

    getTablecloth(): string {
        return "ghosts and skeletons";
    }
}
