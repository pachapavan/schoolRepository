import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';
import { AttendenceDeleteDialogComponent } from '../delete/attendence-delete-dialog.component';

@Component({
  selector: 'jhi-attendence',
  templateUrl: './attendence.component.html',
})
export class AttendenceComponent implements OnInit {
  attendences?: IAttendence[];
  isLoading = false;

  constructor(protected attendenceService: AttendenceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.attendenceService.query().subscribe(
      (res: HttpResponse<IAttendence[]>) => {
        this.isLoading = false;
        this.attendences = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAttendence): number {
    return item.id!;
  }

  delete(attendence: IAttendence): void {
    const modalRef = this.modalService.open(AttendenceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attendence = attendence;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
