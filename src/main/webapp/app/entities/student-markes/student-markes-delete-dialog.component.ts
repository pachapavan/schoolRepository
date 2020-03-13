import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from './student-markes.service';

@Component({
  templateUrl: './student-markes-delete-dialog.component.html'
})
export class StudentMarkesDeleteDialogComponent {
  studentMarkes?: IStudentMarkes;

  constructor(
    protected studentMarkesService: StudentMarkesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentMarkesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('studentMarkesListModification');
      this.activeModal.close();
    });
  }
}
