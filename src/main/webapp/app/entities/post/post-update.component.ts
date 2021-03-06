import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPost, Post } from 'app/shared/model/post.model';
import { PostService } from './post.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';

@Component({
  selector: 'jhi-post-update',
  templateUrl: './post-update.component.html',
})
export class PostUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];
  datePubDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    content: [],
    datePub: [],
    time: [],
    isNameVisibale: [],
    isPhotoVisibale: [],
    nbreLike: [],
    nbreComments: [],
    userId: [],
  });

  constructor(
    protected postService: PostService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ post }) => {
      if (!post.id) {
        const today = moment().startOf('day');
        post.time = today;
      }

      this.updateForm(post);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));
    });
  }

  updateForm(post: IPost): void {
    this.editForm.patchValue({
      id: post.id,
      title: post.title,
      content: post.content,
      datePub: post.datePub,
      time: post.time ? post.time.format(DATE_TIME_FORMAT) : null,
      isNameVisibale: post.isNameVisibale,
      isPhotoVisibale: post.isPhotoVisibale,
      nbreLike: post.nbreLike,
      nbreComments: post.nbreComments,
      userId: post.userId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const post = this.createFromForm();
    if (post.id !== undefined) {
      this.subscribeToSaveResponse(this.postService.update(post));
    } else {
      this.subscribeToSaveResponse(this.postService.create(post));
    }
  }

  private createFromForm(): IPost {
    return {
      ...new Post(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      datePub: this.editForm.get(['datePub'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      isNameVisibale: this.editForm.get(['isNameVisibale'])!.value,
      isPhotoVisibale: this.editForm.get(['isPhotoVisibale'])!.value,
      nbreLike: this.editForm.get(['nbreLike'])!.value,
      nbreComments: this.editForm.get(['nbreComments'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>): void {
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

  trackById(index: number, item: IApplicationUser): any {
    return item.id;
  }
}
