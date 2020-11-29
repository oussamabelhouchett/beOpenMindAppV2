import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFilesPost, FilesPost } from 'app/shared/model/files-post.model';
import { FilesPostService } from './files-post.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';

@Component({
  selector: 'jhi-files-post-update',
  templateUrl: './files-post-update.component.html',
})
export class FilesPostUpdateComponent implements OnInit {
  isSaving = false;
  posts: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    path: [],
    type: [],
    filesPostId: [],
  });

  constructor(
    protected filesPostService: FilesPostService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filesPost }) => {
      this.updateForm(filesPost);

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));
    });
  }

  updateForm(filesPost: IFilesPost): void {
    this.editForm.patchValue({
      id: filesPost.id,
      path: filesPost.path,
      type: filesPost.type,
      filesPostId: filesPost.filesPostId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filesPost = this.createFromForm();
    if (filesPost.id !== undefined) {
      this.subscribeToSaveResponse(this.filesPostService.update(filesPost));
    } else {
      this.subscribeToSaveResponse(this.filesPostService.create(filesPost));
    }
  }

  private createFromForm(): IFilesPost {
    return {
      ...new FilesPost(),
      id: this.editForm.get(['id'])!.value,
      path: this.editForm.get(['path'])!.value,
      type: this.editForm.get(['type'])!.value,
      filesPostId: this.editForm.get(['filesPostId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilesPost>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPost): any {
    return item.id;
  }
}
