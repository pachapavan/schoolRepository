import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttendenceComponent } from '../list/attendence.component';
import { AttendenceDetailComponent } from '../detail/attendence-detail.component';
import { AttendenceUpdateComponent } from '../update/attendence-update.component';
import { AttendenceRoutingResolveService } from './attendence-routing-resolve.service';

const attendenceRoute: Routes = [
  {
    path: '',
    component: AttendenceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttendenceDetailComponent,
    resolve: {
      attendence: AttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attendenceRoute)],
  exports: [RouterModule],
})
export class AttendenceRoutingModule {}
