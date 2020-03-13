import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { StudentFeeComponent } from './student-fee.component';
import { StudentFeeDetailComponent } from './student-fee-detail.component';
import { StudentFeeUpdateComponent } from './student-fee-update.component';
import { StudentFeeDeleteDialogComponent } from './student-fee-delete-dialog.component';
import { studentFeeRoute } from './student-fee.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(studentFeeRoute)],
  declarations: [StudentFeeComponent, StudentFeeDetailComponent, StudentFeeUpdateComponent, StudentFeeDeleteDialogComponent],
  entryComponents: [StudentFeeDeleteDialogComponent]
})
export class JhipsterSampleApplicationStudentFeeModule {}
