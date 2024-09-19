import { WallHangingProvider } from "../../Interfaces/Provider/WallHangingProvider";

export class ChristmasWallHangingProvider implements WallHangingProvider{

    getHanging(): string {
        return "advent calendar";
    }
}
