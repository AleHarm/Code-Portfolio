import { YardOrnamentProvider } from "../../Interfaces/Provider/YardOrnamentProvider";

export class ChristmasYardOrnamentProvider implements YardOrnamentProvider {

    getOrnament(): string {
        return "santa";
    }
}
