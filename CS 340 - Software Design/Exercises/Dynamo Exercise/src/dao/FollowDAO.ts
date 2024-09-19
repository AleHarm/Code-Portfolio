import {
  DeleteCommand,
  DynamoDBDocumentClient,
  GetCommand,
  PutCommand,
  QueryCommand,
  UpdateCommand,
} from "@aws-sdk/lib-dynamodb";
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { FollowObject } from "../entity/FollowObject";  
import { DataPage } from "../entity/DataPage";

export class FollowDAO {
  readonly tableName = "follows";
  readonly indexName = "follows_index";
  readonly follower_handle = "follower_handle";
  readonly followerName = "follower_name";
  readonly followee_handle = "followee_handle";
  readonly followeeName = "followee_name";

  private readonly client = DynamoDBDocumentClient.from(new DynamoDBClient());

  async putFollowObject(followObject: FollowObject): Promise<void> {
    const params = {
      TableName: this.tableName,
      Item: {
        [this.follower_handle]: followObject.follower_handle,
        [this.followerName]: followObject.followerName,
        [this.followee_handle]: followObject.followee_handle,
        [this.followeeName]: followObject.followeeName,
      },
    };
    await this.client.send(new PutCommand(params));
  }

  async updateFollowObject(followObject: FollowObject, newFollowObject: FollowObject): Promise<void> {
    const params = {
      TableName: this.tableName,
      Key: this.generateFollowObject(followObject),
      UpdateExpression:
        "SET " + followObject.follower_handle + " = " + newFollowObject.follower_handle + ","
        + followObject.followerName + " = " + newFollowObject.followerName + ","
        + followObject.followee_handle + " = " + newFollowObject.followee_handle + ","
        + followObject.followeeName + " = " + newFollowObject.followeeName + ","
    };
    await this.client.send(new UpdateCommand(params));
  }

  async getFollowObject(followObject: FollowObject): Promise<FollowObject | undefined> {
    const params = {
      TableName: this.tableName,
      Key: this.generateFollowObject(followObject),
    };
    const output = await this.client.send(new GetCommand(params));
    return output.Item == undefined
      ? undefined
      : new FollowObject(
          output.Item[this.followerName],
          output.Item[this.follower_handle],
          output.Item[this.followeeName],
          output.Item[this.followee_handle],
        );
  }

  async deleteFollowObject(followObject: FollowObject): Promise<void> {
    const params = {
      TableName: this.tableName,
      Key: this.generateFollowObject(followObject),
    };
    await this.client.send(new DeleteCommand(params));
  }

  private generateFollowObject(followObject: FollowObject) {
    return {
      [this.follower_handle]: followObject.follower_handle,
      [this.followerName]: followObject.followerName,
      [this.followee_handle]: followObject.followee_handle,
      [this.followeeName]: followObject.followeeName,
    };
  }

  async getPageOfFollowees(
    followerHandle: string, 
    pageSize: number, 
    lastFolloweeHandle: string | undefined
    ): Promise<DataPage<FollowObject>> {

      const params = {
        KeyConditionExpression: this.follower_handle + " = :f",
        ExpressionAttributeValues: {
          ":f": followerHandle,
        },
        TableName: this.tableName,
        Limit: pageSize,
        ExclusiveStartKey:
          lastFolloweeHandle === undefined
            ? undefined
            : {
                [this.follower_handle]: followerHandle,
                [this.followee_handle]: lastFolloweeHandle,
              },
      };
  
      const items: FollowObject[] = [];
      const data = await this.client.send(new QueryCommand(params));
      const hasMorePages = data.LastEvaluatedKey !== undefined;
      data.Items?.forEach((item) =>
        items.push(
          new FollowObject(
            item[this.follower_handle],
            item[this.followerName],
            item[this.followee_handle],
            item[this.followeeName]
          )
        )
      );
      return new DataPage<FollowObject>(items, hasMorePages);
  }

  async getPageOfFollowers(
    followeeHandle: string,
    pageSize: number,
    lastFollowerHandle: string | undefined
    ): Promise<DataPage<FollowObject>> {


      const params = {
        KeyConditionExpression: this.followee_handle + " = :f",
        ExpressionAttributeValues: {
          ":f": followeeHandle,
        },
        TableName: this.tableName,
        Limit: pageSize,
        ExclusiveStartKey:
          lastFollowerHandle === undefined
            ? undefined
            : {
                [this.followee_handle]: followeeHandle,
                [this.follower_handle]: lastFollowerHandle,
              },
      };
  
      const items: FollowObject[] = [];
      const data = await this.client.send(new QueryCommand(params));
      const hasMorePages = data.LastEvaluatedKey !== undefined;
      data.Items?.forEach((item) =>
        items.push(
          new FollowObject(
            item[this.follower_handle],
            item[this.followerName],
            item[this.followee_handle],
            item[this.followeeName]
          )
        )
      );
      return new DataPage<FollowObject>(items, hasMorePages);
    }
}