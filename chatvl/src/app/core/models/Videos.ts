export class Videos {
  id: number;
  title: string;
  channelId: string;
  channelTitle: string;
  description: string;
  thumbnail: Thumbnail;
  tags: any;
  items: any;
}
class Thumbnail {
  default: ThumbType;
  high: ThumbType;
  maxres: ThumbType;
  medium: ThumbType;
  standard: ThumbType;
}
class ThumbType {
  height: number;
  width: number;
  url: string;
}
