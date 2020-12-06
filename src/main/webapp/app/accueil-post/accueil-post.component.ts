import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NewPostModalComponent } from 'app/accueil-post/new-post-modal/new-post-modal.component';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { IPost, Post } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post/post.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { JhiParseLinks } from 'ng-jhipster';

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
  posts: Post[] = [];

  constructor(private postModalService: NgbModal, private postService: PostService, private parseLinks: JhiParseLinks) {
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
  protected paginatePosts(data: IPost[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.posts.push(data[i]);
      }
    }
  }
  loadPage(page: number): void {
    this.page = page;
    this.loadPost();
  }
  ngOnDestroy(): void {}
}
