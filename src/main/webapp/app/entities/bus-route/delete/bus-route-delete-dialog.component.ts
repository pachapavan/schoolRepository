import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRoute } from '../bus-route.model';
import { BusRouteService } from '../service/bus-route.service';

@Component({
  templateUrl: './bus-route-delete-dialog.component.html',
})
export class BusRouteDeleteDialogComponent {
  busRoute?: IBusRoute;

  constructor(protected busRouteService: BusRouteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busRouteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
