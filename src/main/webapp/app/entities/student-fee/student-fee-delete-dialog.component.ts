import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudentFee } from 'app/shared/model/student-fee.model';
import { StudentFeeService } from './student-fee.service';

@Component({
  templateUrl: './student-fee-delete-dialog.component.html'
})
export class StudentFeeDeleteDialogComponent {
  studentFee?: IStudentFee;

  constructor(
    protected studentFeeService: StudentFeeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentFeeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('studentFeeListModification');
      this.activeModal.close();
    });
  }
}
