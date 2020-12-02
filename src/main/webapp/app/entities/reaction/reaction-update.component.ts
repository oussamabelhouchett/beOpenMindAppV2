import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IReaction, Reaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';

type SelectableEntity = IPost | IApplicationUser;

@Component({
  selector: 'jhi-reaction-update',
  templateUrl: './reaction-update.component.html',
})
export class ReactionUpdateComponent implements OnInit {
  isSaving = false;
  posts: IPost[] = [];
  applicationusers: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    isComment: [],
    isLike: [],
    postId: [],
    userId: [],
  });

  constructor(
    protected reactionService: ReactionService,
    protected postService: PostService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.updateForm(reaction);

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));
    });
  }

  updateForm(reaction: IReaction): void {
    this.editForm.patchValue({
      id: reaction.id,
      isComment: reaction.isComment,
      isLike: reaction.isLike,
      postId: reaction.postId,
      userId: reaction.userId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reaction = this.createFromForm();
    if (reaction.id !== undefined) {
      this.subscribeToSaveResponse(this.reactionService.update(reaction));
    } else {
      this.subscribeToSaveResponse(this.reactionService.create(reaction));
    }
  }

  private createFromForm(): IReaction {
    return {
      ...new Reaction(),
      id: this.editForm.get(['id'])!.value,
      isComment: this.editForm.get(['isComment'])!.value,
      isLike: this.editForm.get(['isLike'])!.value,
      postId: this.editForm.get(['postId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReaction>>): void {
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
