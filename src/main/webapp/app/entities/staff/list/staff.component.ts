import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaff } from '../staff.model';
import { StaffService } from '../service/staff.service';
import { StaffDeleteDialogComponent } from '../delete/staff-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-staff',
  templateUrl: './staff.component.html',
})
export class StaffComponent implements OnInit {
  staff?: IStaff[];
  isLoading = false;

  constructor(protected staffService: StaffService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.staffService.query().subscribe(
      (res: HttpResponse<IStaff[]>) => {
        this.isLoading = false;
        this.staff = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStaff): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(staff: IStaff): void {
    const modalRef = this.modalService.open(StaffDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.staff = staff;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
