import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaffSalary } from 'app/shared/model/staff-salary.model';
import { StaffSalaryService } from './staff-salary.service';
import { StaffSalaryDeleteDialogComponent } from './staff-salary-delete-dialog.component';

@Component({
  selector: 'jhi-staff-salary',
  templateUrl: './staff-salary.component.html'
})
export class StaffSalaryComponent implements OnInit, OnDestroy {
  staffSalaries?: IStaffSalary[];
  eventSubscriber?: Subscription;

  constructor(
    protected staffSalaryService: StaffSalaryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.staffSalaryService.query().subscribe((res: HttpResponse<IStaffSalary[]>) => (this.staffSalaries = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStaffSalaries();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStaffSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStaffSalaries(): void {
    this.eventSubscriber = this.eventManager.subscribe('staffSalaryListModification', () => this.loadAll());
  }

  delete(staffSalary: IStaffSalary): void {
    const modalRef = this.modalService.open(StaffSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.staffSalary = staffSalary;
  }
}
