import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusStops } from 'app/shared/model/bus-stops.model';
import { BusStopsService } from './bus-stops.service';
import { BusStopsDeleteDialogComponent } from './bus-stops-delete-dialog.component';

@Component({
  selector: 'jhi-bus-stops',
  templateUrl: './bus-stops.component.html'
})
export class BusStopsComponent implements OnInit, OnDestroy {
  busStops?: IBusStops[];
  eventSubscriber?: Subscription;

  constructor(protected busStopsService: BusStopsService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.busStopsService.query().subscribe((res: HttpResponse<IBusStops[]>) => (this.busStops = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBusStops();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBusStops): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBusStops(): void {
    this.eventSubscriber = this.eventManager.subscribe('busStopsListModification', () => this.loadAll());
  }

  delete(busStops: IBusStops): void {
    const modalRef = this.modalService.open(BusStopsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busStops = busStops;
  }
}
