import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';
import { AttendenceDeleteDialogComponent } from './attendence-delete-dialog.component';

@Component({
  selector: 'jhi-attendence',
  templateUrl: './attendence.component.html'
})
export class AttendenceComponent implements OnInit, OnDestroy {
  attendences?: IAttendence[];
  eventSubscriber?: Subscription;

  constructor(protected attendenceService: AttendenceService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.attendenceService.query().subscribe((res: HttpResponse<IAttendence[]>) => (this.attendences = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttendences();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttendence): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttendences(): void {
    this.eventSubscriber = this.eventManager.subscribe('attendenceListModification', () => this.loadAll());
  }

  delete(attendence: IAttendence): void {
    const modalRef = this.modalService.open(AttendenceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attendence = attendence;
  }
}
