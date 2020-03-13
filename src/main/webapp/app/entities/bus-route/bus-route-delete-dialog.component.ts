import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusRoute } from 'app/shared/model/bus-route.model';
import { BusRouteService } from './bus-route.service';

@Component({
  templateUrl: './bus-route-delete-dialog.component.html'
})
export class BusRouteDeleteDialogComponent {
  busRoute?: IBusRoute;

  constructor(protected busRouteService: BusRouteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busRouteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('busRouteListModification');
      this.activeModal.close();
    });
  }
}
