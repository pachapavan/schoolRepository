import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentFee } from '../student-fee.model';
import { StudentFeeService } from '../service/student-fee.service';

@Component({
  templateUrl: './student-fee-delete-dialog.component.html',
})
export class StudentFeeDeleteDialogComponent {
  studentFee?: IStudentFee;

  constructor(protected studentFeeService: StudentFeeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentFeeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
