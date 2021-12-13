import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRoute } from '../bus-route.model';
import { BusRouteService } from '../service/bus-route.service';
import { BusRouteDeleteDialogComponent } from '../delete/bus-route-delete-dialog.component';

@Component({
  selector: 'jhi-bus-route',
  templateUrl: './bus-route.component.html',
})
export class BusRouteComponent implements OnInit {
  busRoutes?: IBusRoute[];
  isLoading = false;

  constructor(protected busRouteService: BusRouteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.busRouteService.query().subscribe(
      (res: HttpResponse<IBusRoute[]>) => {
        this.isLoading = false;
        this.busRoutes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBusRoute): number {
    return item.id!;
  }

  delete(busRoute: IBusRoute): void {
    const modalRef = this.modalService.open(BusRouteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busRoute = busRoute;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
