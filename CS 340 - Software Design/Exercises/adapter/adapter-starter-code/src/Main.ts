import { Table } from "./table/Table";
import { ContactManager } from "./contact/ContactManager";
import { TableData } from "./table/TableData";
import { Adapter } from "../src/Adapter"
import { Contact } from "./entity/Contact";

function Main() {
  const contactManager: ContactManager = new ContactManager();

  let contact = new Contact("bill", "billiards", "9", "me@me.me");
  contactManager.addContact(contact);
  contact = new Contact("jeff", "jeffins", "92", "memeasdfasdf@me.me");
  contactManager.addContact(contact);
  contact = new Contact("get", "jeffins", "92d25f", "memeaswryretydfasdf@me.me");
  contactManager.addContact(contact);
  contact = new Contact("asdf", "jeffins", "92fd", "memeasdfassdfgdf@me.me");
  contactManager.addContact(contact);
  contact = new Contact("jebhrergff", "jeffinsvbds", "9227", "memsdfgeasdfasdf@me.me");
  contactManager.addContact(contact);
  contact = new Contact("jeert45ff", "jesdfbffins", "92456", "memeasdsdfgsdfgfasdf@me.me");
  contactManager.addContact(contact);


  const contactsTable: TableData = new Adapter(contactManager)
  const table = new Table(contactsTable, (value: any) => process.stdout.write(value));

  table.display();
}

Main();
