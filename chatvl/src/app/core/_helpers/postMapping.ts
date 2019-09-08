import {Videos} from '@app/core/models/Videos';

export function PostMapping(posts: any) {
  const video = new Videos();
  video.id = posts.items[0].id;
  video.channelId = posts.items[0].snippet.channelId;
  video.channelTitle = posts.items[0].snippet.channelTitle;
  video.title = posts.items[0].snippet.title;
  video.description = posts.items[0].snippet.description;
  video.tags = posts.items[0].snippet.tags;
  video.thumbnail = posts.items[0].snippet.thumbnails;
  return video;
}
