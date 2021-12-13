import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentFeeComponent } from '../list/student-fee.component';
import { StudentFeeDetailComponent } from '../detail/student-fee-detail.component';
import { StudentFeeUpdateComponent } from '../update/student-fee-update.component';
import { StudentFeeRoutingResolveService } from './student-fee-routing-resolve.service';

const studentFeeRoute: Routes = [
  {
    path: '',
    component: StudentFeeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentFeeDetailComponent,
    resolve: {
      studentFee: StudentFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudentFeeUpdateComponent,
    resolve: {
      studentFee: StudentFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudentFeeUpdateComponent,
    resolve: {
      studentFee: StudentFeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentFeeRoute)],
  exports: [RouterModule],
})
export class StudentFeeRoutingModule {}
