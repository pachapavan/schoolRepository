import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StudentMarkesComponent } from './list/student-markes.component';
import { StudentMarkesDetailComponent } from './detail/student-markes-detail.component';
import { StudentMarkesUpdateComponent } from './update/student-markes-update.component';
import { StudentMarkesDeleteDialogComponent } from './delete/student-markes-delete-dialog.component';
import { StudentMarkesRoutingModule } from './route/student-markes-routing.module';

@NgModule({
  imports: [SharedModule, StudentMarkesRoutingModule],
  declarations: [StudentMarkesComponent, StudentMarkesDetailComponent, StudentMarkesUpdateComponent, StudentMarkesDeleteDialogComponent],
  entryComponents: [StudentMarkesDeleteDialogComponent],
})
export class StudentMarkesModule {}
