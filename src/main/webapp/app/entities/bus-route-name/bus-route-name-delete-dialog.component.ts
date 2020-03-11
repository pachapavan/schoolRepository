import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusRouteName } from 'app/shared/model/bus-route-name.model';
import { BusRouteNameService } from './bus-route-name.service';

@Component({
  templateUrl: './bus-route-name-delete-dialog.component.html'
})
export class BusRouteNameDeleteDialogComponent {
  busRouteName?: IBusRouteName;

  constructor(
    protected busRouteNameService: BusRouteNameService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busRouteNameService.delete(id).subscribe(() => {
      this.eventManager.broadcast('busRouteNameListModification');
      this.activeModal.close();
    });
  }
}
