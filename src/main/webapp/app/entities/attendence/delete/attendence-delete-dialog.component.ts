import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

@Component({
  templateUrl: './attendence-delete-dialog.component.html',
})
export class AttendenceDeleteDialogComponent {
  attendence?: IAttendence;

  constructor(protected attendenceService: AttendenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attendenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
