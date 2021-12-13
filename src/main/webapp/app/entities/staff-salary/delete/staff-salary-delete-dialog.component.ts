import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaffSalary } from '../staff-salary.model';
import { StaffSalaryService } from '../service/staff-salary.service';

@Component({
  templateUrl: './staff-salary-delete-dialog.component.html',
})
export class StaffSalaryDeleteDialogComponent {
  staffSalary?: IStaffSalary;

  constructor(protected staffSalaryService: StaffSalaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.staffSalaryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
