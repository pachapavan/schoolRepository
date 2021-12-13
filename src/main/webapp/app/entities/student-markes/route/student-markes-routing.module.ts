import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentMarkesComponent } from '../list/student-markes.component';
import { StudentMarkesDetailComponent } from '../detail/student-markes-detail.component';
import { StudentMarkesUpdateComponent } from '../update/student-markes-update.component';
import { StudentMarkesRoutingResolveService } from './student-markes-routing-resolve.service';

const studentMarkesRoute: Routes = [
  {
    path: '',
    component: StudentMarkesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentMarkesDetailComponent,
    resolve: {
      studentMarkes: StudentMarkesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentMarkesUpdateComponent,
    resolve: {
      studentMarkes: StudentMarkesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentMarkesUpdateComponent,
    resolve: {
      studentMarkes: StudentMarkesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentMarkesRoute)],
  exports: [RouterModule],
})
export class StudentMarkesRoutingModule {}
