import { ContactManager } from "./contact/ContactManager";
import { Justification } from "./entity/Justification";
import { TableData } from "./table/TableData";

export class Adapter implements TableData{

  readonly ROW_COUNT = 4;
  readonly SPACING = 2;
  readonly HEADER_UNDERLINE = "";
  readonly JUSTIFICATION: Justification = Justification.Center;
  private contactManager: ContactManager;



  constructor(contactManager: ContactManager) {
    this.contactManager = contactManager;
  }

  getColumnCount(): number {
    return this.ROW_COUNT;
  }
  getRowCount(): number {
    return this.contactManager.getContactCount();
  }
  getColumnSpacing(): number {
    return this.SPACING;
  }
  getRowSpacing(): number {
    return this.SPACING;
  }
  getHeaderUnderline(): string {
    return this.HEADER_UNDERLINE;
  }
  getColumnHeader(col: number): string {
    switch(col){

      case 1:
        return "firstName";

      case 2:
        return "lastName";

      case 3:
        return "phone";

      case 4:
        return "email";
      
      default:
        return "Column not specified"
    }
  }
  getColumnWidth(col: number): number {
    return this.SPACING;
  }
  getColumnJustification(col: number): Justification {
    return this.JUSTIFICATION;
  }
  getCellValue(row: number, col: number): string {
    switch(col){

      case 1:
        return this.contactManager.getContact(row).firstName;

      case 2:
        return this.contactManager.getContact(row).lastName;

      case 3:
        return this.contactManager.getContact(row).phone;

      case 4:
        return this.contactManager.getContact(row).email;
      
      default:
        return "Column not specified"
    }
  }
}