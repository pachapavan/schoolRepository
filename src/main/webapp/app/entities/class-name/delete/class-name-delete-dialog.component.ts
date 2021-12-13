import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassName } from '../class-name.model';
import { ClassNameService } from '../service/class-name.service';

@Component({
  templateUrl: './class-name-delete-dialog.component.html',
})
export class ClassNameDeleteDialogComponent {
  className?: IClassName;

  constructor(protected classNameService: ClassNameService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classNameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
