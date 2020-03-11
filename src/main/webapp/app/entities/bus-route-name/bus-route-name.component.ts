import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRouteName } from 'app/shared/model/bus-route-name.model';
import { BusRouteNameService } from './bus-route-name.service';
import { BusRouteNameDeleteDialogComponent } from './bus-route-name-delete-dialog.component';

@Component({
  selector: 'jhi-bus-route-name',
  templateUrl: './bus-route-name.component.html'
})
export class BusRouteNameComponent implements OnInit, OnDestroy {
  busRouteNames?: IBusRouteName[];
  eventSubscriber?: Subscription;

  constructor(
    protected busRouteNameService: BusRouteNameService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.busRouteNameService.query().subscribe((res: HttpResponse<IBusRouteName[]>) => (this.busRouteNames = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBusRouteNames();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBusRouteName): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBusRouteNames(): void {
    this.eventSubscriber = this.eventManager.subscribe('busRouteNameListModification', () => this.loadAll());
  }

  delete(busRouteName: IBusRouteName): void {
    const modalRef = this.modalService.open(BusRouteNameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busRouteName = busRouteName;
  }
}
