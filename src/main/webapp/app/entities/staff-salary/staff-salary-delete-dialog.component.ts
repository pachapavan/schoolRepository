import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStaffSalary } from 'app/shared/model/staff-salary.model';
import { StaffSalaryService } from './staff-salary.service';

@Component({
  templateUrl: './staff-salary-delete-dialog.component.html'
})
export class StaffSalaryDeleteDialogComponent {
  staffSalary?: IStaffSalary;

  constructor(
    protected staffSalaryService: StaffSalaryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.staffSalaryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('staffSalaryListModification');
      this.activeModal.close();
    });
  }
}
