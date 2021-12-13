import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassName } from '../class-name.model';
import { ClassNameService } from '../service/class-name.service';
import { ClassNameDeleteDialogComponent } from '../delete/class-name-delete-dialog.component';

@Component({
  selector: 'jhi-class-name',
  templateUrl: './class-name.component.html',
})
export class ClassNameComponent implements OnInit {
  classNames?: IClassName[];
  isLoading = false;

  constructor(protected classNameService: ClassNameService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.classNameService.query().subscribe(
      (res: HttpResponse<IClassName[]>) => {
        this.isLoading = false;
        this.classNames = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IClassName): number {
    return item.id!;
  }

  delete(className: IClassName): void {
    const modalRef = this.modalService.open(ClassNameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.className = className;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
