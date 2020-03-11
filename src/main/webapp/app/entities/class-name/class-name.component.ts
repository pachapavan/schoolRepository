import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassName } from 'app/shared/model/class-name.model';
import { ClassNameService } from './class-name.service';
import { ClassNameDeleteDialogComponent } from './class-name-delete-dialog.component';

@Component({
  selector: 'jhi-class-name',
  templateUrl: './class-name.component.html'
})
export class ClassNameComponent implements OnInit, OnDestroy {
  classNames?: IClassName[];
  eventSubscriber?: Subscription;

  constructor(protected classNameService: ClassNameService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.classNameService.query().subscribe((res: HttpResponse<IClassName[]>) => (this.classNames = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInClassNames();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IClassName): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInClassNames(): void {
    this.eventSubscriber = this.eventManager.subscribe('classNameListModification', () => this.loadAll());
  }

  delete(className: IClassName): void {
    const modalRef = this.modalService.open(ClassNameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.className = className;
  }
}
