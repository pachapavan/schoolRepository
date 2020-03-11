import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentFee } from 'app/shared/model/student-fee.model';
import { StudentFeeService } from './student-fee.service';
import { StudentFeeDeleteDialogComponent } from './student-fee-delete-dialog.component';

@Component({
  selector: 'jhi-student-fee',
  templateUrl: './student-fee.component.html'
})
export class StudentFeeComponent implements OnInit, OnDestroy {
  studentFees?: IStudentFee[];
  eventSubscriber?: Subscription;

  constructor(protected studentFeeService: StudentFeeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.studentFeeService.query().subscribe((res: HttpResponse<IStudentFee[]>) => (this.studentFees = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStudentFees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStudentFee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStudentFees(): void {
    this.eventSubscriber = this.eventManager.subscribe('studentFeeListModification', () => this.loadAll());
  }

  delete(studentFee: IStudentFee): void {
    const modalRef = this.modalService.open(StudentFeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentFee = studentFee;
  }
}
