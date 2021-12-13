import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentMarkes } from '../student-markes.model';
import { StudentMarkesService } from '../service/student-markes.service';
import { StudentMarkesDeleteDialogComponent } from '../delete/student-markes-delete-dialog.component';

@Component({
  selector: 'jhi-student-markes',
  templateUrl: './student-markes.component.html',
})
export class StudentMarkesComponent implements OnInit {
  studentMarkes?: IStudentMarkes[];
  isLoading = false;

  constructor(protected studentMarkesService: StudentMarkesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.studentMarkesService.query().subscribe(
      (res: HttpResponse<IStudentMarkes[]>) => {
        this.isLoading = false;
        this.studentMarkes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStudentMarkes): number {
    return item.id!;
  }

  delete(studentMarkes: IStudentMarkes): void {
    const modalRef = this.modalService.open(StudentMarkesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentMarkes = studentMarkes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
