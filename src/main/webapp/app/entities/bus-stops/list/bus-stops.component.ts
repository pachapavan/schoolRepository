import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusStops } from '../bus-stops.model';
import { BusStopsService } from '../service/bus-stops.service';
import { BusStopsDeleteDialogComponent } from '../delete/bus-stops-delete-dialog.component';

@Component({
  selector: 'jhi-bus-stops',
  templateUrl: './bus-stops.component.html',
})
export class BusStopsComponent implements OnInit {
  busStops?: IBusStops[];
  isLoading = false;

  constructor(protected busStopsService: BusStopsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.busStopsService.query().subscribe(
      (res: HttpResponse<IBusStops[]>) => {
        this.isLoading = false;
        this.busStops = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBusStops): number {
    return item.id!;
  }

  delete(busStops: IBusStops): void {
    const modalRef = this.modalService.open(BusStopsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busStops = busStops;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
