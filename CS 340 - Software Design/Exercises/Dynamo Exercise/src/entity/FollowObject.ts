export class FollowObject {
  follower_handle: string;
  followerName: string;
  followee_handle: string;
  followeeName: string;


  constructor(
   follower_handle: string,
   followerName: string,
   followee_handle: string,
   followeeName: string
  ) {
    this.follower_handle = follower_handle;
    this.followerName = followerName;
    this.followee_handle = followee_handle;
    this.followeeName = followeeName;
  }

  toString(): string {
    return (
      "follows{" +
      "follower_handle='" +
      this.follower_handle +
      "'" +
      ", followerName='" +
      this.followerName +
      "'" +
      ", followee_handle=" +
      this.followeeName +
      "'" +
      ", followeeName='" +
      this.followeeName +
      "'" +
      "}"
    );
  }
}