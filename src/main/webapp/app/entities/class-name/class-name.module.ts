import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClassNameComponent } from './list/class-name.component';
import { ClassNameDetailComponent } from './detail/class-name-detail.component';
import { ClassNameUpdateComponent } from './update/class-name-update.component';
import { ClassNameDeleteDialogComponent } from './delete/class-name-delete-dialog.component';
import { ClassNameRoutingModule } from './route/class-name-routing.module';

@NgModule({
  imports: [SharedModule, ClassNameRoutingModule],
  declarations: [ClassNameComponent, ClassNameDetailComponent, ClassNameUpdateComponent, ClassNameDeleteDialogComponent],
  entryComponents: [ClassNameDeleteDialogComponent],
})
export class ClassNameModule {}
