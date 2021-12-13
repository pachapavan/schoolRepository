import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StaffSalaryComponent } from './list/staff-salary.component';
import { StaffSalaryDetailComponent } from './detail/staff-salary-detail.component';
import { StaffSalaryUpdateComponent } from './update/staff-salary-update.component';
import { StaffSalaryDeleteDialogComponent } from './delete/staff-salary-delete-dialog.component';
import { StaffSalaryRoutingModule } from './route/staff-salary-routing.module';

@NgModule({
  imports: [SharedModule, StaffSalaryRoutingModule],
  declarations: [StaffSalaryComponent, StaffSalaryDetailComponent, StaffSalaryUpdateComponent, StaffSalaryDeleteDialogComponent],
  entryComponents: [StaffSalaryDeleteDialogComponent],
})
export class StaffSalaryModule {}
