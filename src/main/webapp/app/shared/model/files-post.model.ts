export interface IFilesPost {
  id?: number;
  path?: string;
  type?: string;
  filesPostId?: number;
}

export class FilesPost implements IFilesPost {
  constructor(public id?: number, public path?: string, public type?: string, public filesPostId?: number) {}
}
