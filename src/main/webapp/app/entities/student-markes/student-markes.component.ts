import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from './student-markes.service';
import { StudentMarkesDeleteDialogComponent } from './student-markes-delete-dialog.component';

@Component({
  selector: 'jhi-student-markes',
  templateUrl: './student-markes.component.html'
})
export class StudentMarkesComponent implements OnInit, OnDestroy {
  studentMarkes?: IStudentMarkes[];
  eventSubscriber?: Subscription;

  constructor(
    protected studentMarkesService: StudentMarkesService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.studentMarkesService.query().subscribe((res: HttpResponse<IStudentMarkes[]>) => (this.studentMarkes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStudentMarkes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStudentMarkes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStudentMarkes(): void {
    this.eventSubscriber = this.eventManager.subscribe('studentMarkesListModification', () => this.loadAll());
  }

  delete(studentMarkes: IStudentMarkes): void {
    const modalRef = this.modalService.open(StudentMarkesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.studentMarkes = studentMarkes;
  }
}
