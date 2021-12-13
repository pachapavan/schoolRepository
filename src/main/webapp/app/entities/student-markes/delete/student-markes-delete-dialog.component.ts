import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentMarkes } from '../student-markes.model';
import { StudentMarkesService } from '../service/student-markes.service';

@Component({
  templateUrl: './student-markes-delete-dialog.component.html',
})
export class StudentMarkesDeleteDialogComponent {
  studentMarkes?: IStudentMarkes;

  constructor(protected studentMarkesService: StudentMarkesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentMarkesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
