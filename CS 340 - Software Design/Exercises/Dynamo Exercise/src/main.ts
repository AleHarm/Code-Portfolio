import { FollowObject } from "./entity/FollowObject";
// import { DataPage } from "./entity/DataPage";
import { FollowDAO } from "./dao/FollowDAO";

class main{

  async run(){

    const dao = new FollowDAO();

    //Same follower x25
    for(let i = 0; i < 25; i++){

      const newFollow = new FollowObject("@TimTimmins", "Tim Timmins", "@" + i, i.toString());
      await dao.putFollowObject(newFollow);
    }

    //Same followee x25
    for(let i = 0; i < 25; i++){

      const newFollow = new FollowObject("@" + i, i.toString(), "@JimJimmins", "Jim Jimmins");
      await dao.putFollowObject(newFollow);
    }

    const newFollow = new FollowObject("@TimTimmins", "Tim Timmins", "@20", "20");
    const gotObject = await dao.getFollowObject(newFollow);

    const updateFollow = new FollowObject("@TimTimTimmins", "Time Timmins", "@20", "20");
    await dao.updateFollowObject(newFollow, updateFollow);

    await dao.deleteFollowObject(updateFollow);


    await dao.getPageOfFollowers("@TimTimTimmins", 2, "@20")
  }
}

function run() {
  new main().run();
}

run();