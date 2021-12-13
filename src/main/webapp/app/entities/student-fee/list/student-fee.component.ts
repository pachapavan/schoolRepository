import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentFee } from '../student-fee.model';
import { StudentFeeService } from '../service/student-fee.service';
import { StudentFeeDeleteDialogComponent } from '../delete/student-fee-delete-dialog.component';

@Component({
  selector: 'jhi-student-fee',
  templateUrl: './student-fee.component.html',
})
export class StudentFeeComponent implements OnInit {
  studentFees?: IStudentFee[];
  isLoading = false;

  constructor(protected studentFeeService: StudentFeeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.studentFeeService.query().subscribe(
      (res: HttpResponse<IStudentFee[]>) => {
        this.isLoading = false;
        this.studentFees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStudentFee): number {
    return item.id!;
  }

  delete(studentFee: IStudentFee): void {
    const modalRef = this.modalService.open(StudentFeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentFee = studentFee;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
