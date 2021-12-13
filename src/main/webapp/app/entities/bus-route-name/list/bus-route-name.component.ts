import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRouteName } from '../bus-route-name.model';
import { BusRouteNameService } from '../service/bus-route-name.service';
import { BusRouteNameDeleteDialogComponent } from '../delete/bus-route-name-delete-dialog.component';

@Component({
  selector: 'jhi-bus-route-name',
  templateUrl: './bus-route-name.component.html',
})
export class BusRouteNameComponent implements OnInit {
  busRouteNames?: IBusRouteName[];
  isLoading = false;

  constructor(protected busRouteNameService: BusRouteNameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.busRouteNameService.query().subscribe(
      (res: HttpResponse<IBusRouteName[]>) => {
        this.isLoading = false;
        this.busRouteNames = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBusRouteName): number {
    return item.id!;
  }

  delete(busRouteName: IBusRouteName): void {
    const modalRef = this.modalService.open(BusRouteNameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.busRouteName = busRouteName;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
