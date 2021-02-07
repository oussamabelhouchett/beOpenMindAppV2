import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NewPostModalComponent } from 'app/accueil-post/new-post-modal/new-post-modal.component';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JhiParseLinks } from 'ng-jhipster';
import { ReactionService } from 'app/entities/reaction/reaction.service';
import { map } from 'rxjs/operators';
import { Reaction } from 'app/shared/model/reaction.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-accueil-post',
  templateUrl: './accueil-post.component.html',
})
export class AccueilPostComponent implements OnInit, OnDestroy {
  private modelRef: NgbModalRef | undefined;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  posts: IPost[] = [];
  currentUserId = 4;

  constructor(
    private postModalService: NgbModal,
    private postService: PostService,
    private parseLinks: JhiParseLinks,
    private reactionService: ReactionService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }
  ngOnInit(): void {
    this.loadPost();
  }
  loadPost(): void {
    this.postService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPost[]>) => this.paginatePosts(res.body, res.headers));
  }
  openNewPostmodal(): void {
    this.modelRef = this.postModalService.open(NewPostModalComponent as Component, {
      windowClass: 'my-class',
    });
    this.modelRef.componentInstance.modelRef = this.modelRef;
    this.modelRef.componentInstance.parent = this;
  }
  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  private paginatePosts(data: IPost[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.checkReactionCurrentuserBypost(data[i]);
        this.posts.push(data[i]);
      }
    }
  }
  private checkReactionCurrentuserBypost(post: IPost): void {
    this.reactionService
      .query({
        'postId.equals': post.id,
        'userId.equals': this.currentUserId,
        'isLike.equals': 1,
      })
      .pipe(map(responce => responce.body![0]))
      .subscribe(reaction => {
        // eslint-disable-next-line no-console
        console.log('---------------------------------------', reaction);
        post.currentUserReaction = reaction || new Reaction();
      });
  }
  loadPage(page: number): void {
    this.page = page;
    this.loadPost();
  }
  ngOnDestroy(): void {}
  like(post: IPost): void {
    post.currentUserReaction!.isLike = post.currentUserReaction!.isLike ? 0 : 1;
    post.nbreLike = (post.nbreLike || 0) + (post.currentUserReaction!.isLike ? 1 : -1);
    if (post.currentUserReaction && post.currentUserReaction.id) {
      this.subscribeToSaveResponse(this.reactionService.update(post.currentUserReaction));
    } else {
      post.currentUserReaction!.postId = post.id;
      post.currentUserReaction!.userId = this.currentUserId;
      this.reactionService.create(post.currentUserReaction!).subscribe(res => {
        if (res.body && res.body.id) post.currentUserReaction!.id = res.body.id;
      });
    }
    this.subscribeToSaveResponse(this.postService.update(post));
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {}

  protected onSaveError(): void {}
}
