import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReaction } from 'app/shared/model/reaction.model';
import { ReactionService } from './reaction.service';
import { ReactionDeleteDialogComponent } from './reaction-delete-dialog.component';

@Component({
  selector: 'jhi-reaction',
  templateUrl: './reaction.component.html',
})
export class ReactionComponent implements OnInit, OnDestroy {
  reactions?: IReaction[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected reactionService: ReactionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.reactionService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IReaction[]>) => (this.reactions = res.body || []));
      return;
    }

    this.reactionService.query().subscribe((res: HttpResponse<IReaction[]>) => (this.reactions = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReactions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReaction): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReactions(): void {
    this.eventSubscriber = this.eventManager.subscribe('reactionListModification', () => this.loadAll());
  }

  delete(reaction: IReaction): void {
    const modalRef = this.modalService.open(ReactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reaction = reaction;
  }
}
