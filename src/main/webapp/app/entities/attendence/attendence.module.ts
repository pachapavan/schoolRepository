import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttendenceComponent } from './list/attendence.component';
import { AttendenceDetailComponent } from './detail/attendence-detail.component';
import { AttendenceUpdateComponent } from './update/attendence-update.component';
import { AttendenceDeleteDialogComponent } from './delete/attendence-delete-dialog.component';
import { AttendenceRoutingModule } from './route/attendence-routing.module';

@NgModule({
  imports: [SharedModule, AttendenceRoutingModule],
  declarations: [AttendenceComponent, AttendenceDetailComponent, AttendenceUpdateComponent, AttendenceDeleteDialogComponent],
  entryComponents: [AttendenceDeleteDialogComponent],
})
export class AttendenceModule {}
