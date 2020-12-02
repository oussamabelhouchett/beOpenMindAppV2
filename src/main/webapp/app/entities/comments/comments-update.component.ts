import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IComments, Comments } from 'app/shared/model/comments.model';
import { CommentsService } from './comments.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';

type SelectableEntity = IComments | IPost;

@Component({
  selector: 'jhi-comments-update',
  templateUrl: './comments-update.component.html',
})
export class CommentsUpdateComponent implements OnInit {
  isSaving = false;
  commentsCollection: IComments[] = [];
  posts: IPost[] = [];
  datePubDp: any;

  editForm = this.fb.group({
    id: [],
    contentText: [],
    datePub: [],
    time: [],
    parentId: [],
    postId: [],
  });

  constructor(
    protected commentsService: CommentsService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comments }) => {
      if (!comments.id) {
        const today = moment().startOf('day');
        comments.time = today;
      }

      this.updateForm(comments);

      this.commentsService.query().subscribe((res: HttpResponse<IComments[]>) => (this.commentsCollection = res.body || []));

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));
    });
  }

  updateForm(comments: IComments): void {
    this.editForm.patchValue({
      id: comments.id,
      contentText: comments.contentText,
      datePub: comments.datePub,
      time: comments.time ? comments.time.format(DATE_TIME_FORMAT) : null,
      parentId: comments.parentId,
      postId: comments.postId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comments = this.createFromForm();
    if (comments.id !== undefined) {
      this.subscribeToSaveResponse(this.commentsService.update(comments));
    } else {
      this.subscribeToSaveResponse(this.commentsService.create(comments));
    }
  }

  private createFromForm(): IComments {
    return {
      ...new Comments(),
      id: this.editForm.get(['id'])!.value,
      contentText: this.editForm.get(['contentText'])!.value,
      datePub: this.editForm.get(['datePub'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      parentId: this.editForm.get(['parentId'])!.value,
      postId: this.editForm.get(['postId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComments>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
