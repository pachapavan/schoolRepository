import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusRouteName } from '../bus-route-name.model';
import { BusRouteNameService } from '../service/bus-route-name.service';

@Component({
  templateUrl: './bus-route-name-delete-dialog.component.html',
})
export class BusRouteNameDeleteDialogComponent {
  busRouteName?: IBusRouteName;

  constructor(protected busRouteNameService: BusRouteNameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busRouteNameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
