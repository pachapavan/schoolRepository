import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { StaffSalaryComponent } from './staff-salary.component';
import { StaffSalaryDetailComponent } from './staff-salary-detail.component';
import { StaffSalaryUpdateComponent } from './staff-salary-update.component';
import { StaffSalaryDeleteDialogComponent } from './staff-salary-delete-dialog.component';
import { staffSalaryRoute } from './staff-salary.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(staffSalaryRoute)],
  declarations: [StaffSalaryComponent, StaffSalaryDetailComponent, StaffSalaryUpdateComponent, StaffSalaryDeleteDialogComponent],
  entryComponents: [StaffSalaryDeleteDialogComponent]
})
export class JhipsterSampleApplicationStaffSalaryModule {}
