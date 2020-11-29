import { Moment } from 'moment';
import { IComments } from 'app/shared/model/comments.model';
import { IFilesPost } from 'app/shared/model/files-post.model';

export interface IPost {
  id?: number;
  title?: string;
  content?: string;
  datePub?: Moment;
  time?: Moment;
  comments?: IComments[];
  filesPosts?: IFilesPost[];
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public title?: string,
    public content?: string,
    public datePub?: Moment,
    public time?: Moment,
    public comments?: IComments[],
    public filesPosts?: IFilesPost[]
  ) {}
}
