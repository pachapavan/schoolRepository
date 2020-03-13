import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { StudentMarkesComponent } from './student-markes.component';
import { StudentMarkesDetailComponent } from './student-markes-detail.component';
import { StudentMarkesUpdateComponent } from './student-markes-update.component';
import { StudentMarkesDeleteDialogComponent } from './student-markes-delete-dialog.component';
import { studentMarkesRoute } from './student-markes.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(studentMarkesRoute)],
  declarations: [StudentMarkesComponent, StudentMarkesDetailComponent, StudentMarkesUpdateComponent, StudentMarkesDeleteDialogComponent],
  entryComponents: [StudentMarkesDeleteDialogComponent]
})
export class JhipsterSampleApplicationStudentMarkesModule {}
