import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilesPost } from 'app/shared/model/files-post.model';

@Component({
  selector: 'jhi-files-post-detail',
  templateUrl: './files-post-detail.component.html',
})
export class FilesPostDetailComponent implements OnInit {
  filesPost: IFilesPost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filesPost }) => (this.filesPost = filesPost));
  }

  previousState(): void {
    window.history.back();
  }
}
