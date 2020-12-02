import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IApplicationUser, ApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from './application-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';
import { IComments } from 'app/shared/model/comments.model';
import { CommentsService } from 'app/entities/comments/comments.service';
import { IReaction } from 'app/shared/model/reaction.model';
import { ReactionService } from 'app/entities/reaction/reaction.service';

type SelectableEntity = IUser | IPost | IComments | IReaction;

@Component({
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  posts: IPost[] = [];
  comments: IComments[] = [];
  reactions: IReaction[] = [];

  editForm = this.fb.group({
    id: [],
    additionalField: [null, [Validators.min(42), Validators.max(42)]],
    userId: [],
    postId: [],
    commentsId: [],
    reactionId: [],
  });

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected userService: UserService,
    protected postService: PostService,
    protected commentsService: CommentsService,
    protected reactionService: ReactionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.updateForm(applicationUser);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));

      this.commentsService.query().subscribe((res: HttpResponse<IComments[]>) => (this.comments = res.body || []));

      this.reactionService.query().subscribe((res: HttpResponse<IReaction[]>) => (this.reactions = res.body || []));
    });
  }

  updateForm(applicationUser: IApplicationUser): void {
    this.editForm.patchValue({
      id: applicationUser.id,
      additionalField: applicationUser.additionalField,
      userId: applicationUser.userId,
      postId: applicationUser.postId,
      commentsId: applicationUser.commentsId,
      reactionId: applicationUser.reactionId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.createFromForm();
    if (applicationUser.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  private createFromForm(): IApplicationUser {
    return {
      ...new ApplicationUser(),
      id: this.editForm.get(['id'])!.value,
      additionalField: this.editForm.get(['additionalField'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      postId: this.editForm.get(['postId'])!.value,
      commentsId: this.editForm.get(['commentsId'])!.value,
      reactionId: this.editForm.get(['reactionId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
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
