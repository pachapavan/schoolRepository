import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaffSalary } from '../staff-salary.model';
import { StaffSalaryService } from '../service/staff-salary.service';
import { StaffSalaryDeleteDialogComponent } from '../delete/staff-salary-delete-dialog.component';

@Component({
  selector: 'jhi-staff-salary',
  templateUrl: './staff-salary.component.html',
})
export class StaffSalaryComponent implements OnInit {
  staffSalaries?: IStaffSalary[];
  isLoading = false;

  constructor(protected staffSalaryService: StaffSalaryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.staffSalaryService.query().subscribe(
      (res: HttpResponse<IStaffSalary[]>) => {
        this.isLoading = false;
        this.staffSalaries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStaffSalary): number {
    return item.id!;
  }

  delete(staffSalary: IStaffSalary): void {
    const modalRef = this.modalService.open(StaffSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.staffSalary = staffSalary;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
