type Sock = {
  style: string;
  color: string;
}

type Shirt = {

  style: string;
  size: string;
}

type Pants = {

  waist: number;
  length: number;
}

class Drawer<T> {
  items: Array<T>;
  isEmpty(): boolean {
    if (this.items.length === 0) {
      return true;
    } else {
      return false;
    }
  }

  addItem(item: T): void{

    this.items.push(item);
  }

  removeItem(): any{

    return this.items.shift();
  }

  removeAll(): Array<T> {
    const removedItems = this.items.slice();
    this.items.length = 0;
    return removedItems;
  }
}

class Dresser<T, U, V>{

  public topDrawer: Drawer<T>;
  public middleDrawer: Drawer<U>;
  public bottomDrawer: Drawer<V>;

  public constructor() {

    this.topDrawer = new Drawer<T>();
    this.middleDrawer = new Drawer<U>();
    this.bottomDrawer = new Drawer<V>();
  }

}

function main(){

  const sock1: Sock = {
    style: "tall",
    color: "Red"
  };

  const sock2: Sock = {
    style: "short",
    color: "blue"
  };

  const sock3: Sock = {
    style: "boring",
    color: "white"
  };

  const shirt1 :Shirt = {

    style: "short",
    size: "medium"
  }

  const shirt2 :Shirt = {

    style: "long",
    size: "medium"
  }

  const shirt3 :Shirt = {

    style: "sweater",
    size: "medium"
  }

  const pants1 :Pants = {

    waist: 15,
    length: 12
  }

  const pants2 :Pants = {

    waist: 20,
    length: 18
  }

  const pants3 :Pants = {

    waist: 25,
    length: 20
  }

  const dresser = new Dresser<Sock, Shirt, Pants>();

  if(dresser.topDrawer.isEmpty()){

    console.log("The top drawer is empty!");
  }

  dresser.topDrawer.addItem(sock1);
  dresser.topDrawer.addItem(sock2);
  dresser.topDrawer.addItem(sock3);

  dresser.middleDrawer.addItem(shirt1);
  dresser.middleDrawer.addItem(shirt2);
  dresser.middleDrawer.addItem(shirt3);

  dresser.bottomDrawer.addItem(pants1);
  dresser.bottomDrawer.addItem(pants2);
  dresser.bottomDrawer.addItem(pants3);

  console.log(dresser.topDrawer.removeItem());

  console.log(dresser.middleDrawer.removeAll());
}