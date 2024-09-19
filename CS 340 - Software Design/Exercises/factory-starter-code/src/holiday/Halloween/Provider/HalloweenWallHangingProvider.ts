import { WallHangingProvider } from "../../Interfaces/Provider/WallHangingProvider";

export class HalloweenWallHangingProvider implements WallHangingProvider{

    getHanging(): string {
        return "spider-web";
    }
}
