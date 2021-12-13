import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusStops } from '../bus-stops.model';
import { BusStopsService } from '../service/bus-stops.service';

@Component({
  templateUrl: './bus-stops-delete-dialog.component.html',
})
export class BusStopsDeleteDialogComponent {
  busStops?: IBusStops;

  constructor(protected busStopsService: BusStopsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busStopsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
