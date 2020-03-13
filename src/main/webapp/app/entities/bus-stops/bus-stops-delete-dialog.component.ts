import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusStops } from 'app/shared/model/bus-stops.model';
import { BusStopsService } from './bus-stops.service';

@Component({
  templateUrl: './bus-stops-delete-dialog.component.html'
})
export class BusStopsDeleteDialogComponent {
  busStops?: IBusStops;

  constructor(protected busStopsService: BusStopsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busStopsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('busStopsListModification');
      this.activeModal.close();
    });
  }
}
