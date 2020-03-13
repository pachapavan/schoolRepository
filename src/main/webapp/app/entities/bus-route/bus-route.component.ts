import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRoute } from 'app/shared/model/bus-route.model';
import { BusRouteService } from './bus-route.service';
import { BusRouteDeleteDialogComponent } from './bus-route-delete-dialog.component';

@Component({
  selector: 'jhi-bus-route',
  templateUrl: './bus-route.component.html'
})
export class BusRouteComponent implements OnInit, OnDestroy {
  busRoutes?: IBusRoute[];
  eventSubscriber?: Subscription;

  constructor(protected busRouteService: BusRouteService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.busRouteService.query().subscribe((res: HttpResponse<IBusRoute[]>) => (this.busRoutes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBusRoutes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBusRoute): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBusRoutes(): void {
    this.eventSubscriber = this.eventManager.subscribe('busRouteListModification', () => this.loadAll());
  }

  delete(busRoute: IBusRoute): void {
    const modalRef = this.modalService.open(BusRouteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busRoute = busRoute;
  }
}
