import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassName } from 'app/shared/model/class-name.model';
import { ClassNameService } from './class-name.service';

@Component({
  templateUrl: './class-name-delete-dialog.component.html'
})
export class ClassNameDeleteDialogComponent {
  className?: IClassName;

  constructor(protected classNameService: ClassNameService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classNameService.delete(id).subscribe(() => {
      this.eventManager.broadcast('classNameListModification');
      this.activeModal.close();
    });
  }
}
