import { Component, OnInit } from '@angular/core';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { Account } from 'app/core/user/account.model';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IPost, Post } from 'app/shared/model/post.model';

import { PostService } from 'app/entities/post/post.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { AccueilPostComponent } from 'app/accueil-post/accueil-post.component';
@Component({
  selector: 'jhi-new-post-modal',
  templateUrl: './new-post-modal.component.html',
  styleUrls: ['./new-post-modal.component.scss'],
})
export class NewPostModalComponent implements OnInit {
  modelRef: NgbModalRef | undefined;
  currentAccount: Account | undefined;
  parent: AccueilPostComponent | undefined;
  post: Post = {};

  constructor(private stateStorageService: StateStorageService, private postService: PostService) {}

  ngOnInit(): void {
    this.currentAccount = this.stateStorageService.getCurrentAccount() as Account | undefined;
  }
  doTextareaValueChange(ev: any): void {
    this.post.content = ev.target.value || '';
  }
  save(): void {
    this.perparePostInstance();
    this.subscribeToSaveResponse(this.postService.create(this.post));
  }
  protected perparePostInstance(): void {
    this.post.datePub = moment(new Date());
    const today = moment().startOf('day');
    this.post.time = today;
    this.post.userId = this.currentAccount!.id || 0;
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (this.parent) {
      this.parent.loadPost();
    }
    if (this.modelRef) {
      this.modelRef.close();
    }
  }

  protected onSaveError(): void {}
}
