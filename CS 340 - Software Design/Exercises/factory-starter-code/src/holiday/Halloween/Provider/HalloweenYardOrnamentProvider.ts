import { YardOrnamentProvider } from "../../Interfaces/Provider/YardOrnamentProvider";

export class HalloweenYardOrnamentProvider implements YardOrnamentProvider {

    getOrnament(): string {
        return "jack-o-lantern";
    }
}
