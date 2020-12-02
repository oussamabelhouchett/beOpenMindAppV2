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

@Component({
  selector: 'jhi-reaction-update',
  templateUrl: './reaction-update.component.html',
})
export class ReactionUpdateComponent implements OnInit {
  isSaving = false;
  posts: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    isComment: [],
    isLike: [],
    postId: [],
  });

  constructor(
    protected reactionService: ReactionService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.updateForm(reaction);

      this.postService.query().subscribe((res: HttpResponse<IPost[]>) => (this.posts = res.body || []));
    });
  }

  updateForm(reaction: IReaction): void {
    this.editForm.patchValue({
      id: reaction.id,
      isComment: reaction.isComment,
      isLike: reaction.isLike,
      postId: reaction.postId,
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

  trackById(index: number, item: IPost): any {
    return item.id;
  }
}
