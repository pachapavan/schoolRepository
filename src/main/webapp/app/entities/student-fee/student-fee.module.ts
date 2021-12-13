import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StudentFeeComponent } from './list/student-fee.component';
import { StudentFeeDetailComponent } from './detail/student-fee-detail.component';
import { StudentFeeUpdateComponent } from './update/student-fee-update.component';
import { StudentFeeDeleteDialogComponent } from './delete/student-fee-delete-dialog.component';
import { StudentFeeRoutingModule } from './route/student-fee-routing.module';

@NgModule({
  imports: [SharedModule, StudentFeeRoutingModule],
  declarations: [StudentFeeComponent, StudentFeeDetailComponent, StudentFeeUpdateComponent, StudentFeeDeleteDialogComponent],
  entryComponents: [StudentFeeDeleteDialogComponent],
})
export class StudentFeeModule {}
