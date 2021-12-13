import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StaffSalaryComponent } from '../list/staff-salary.component';
import { StaffSalaryDetailComponent } from '../detail/staff-salary-detail.component';
import { StaffSalaryUpdateComponent } from '../update/staff-salary-update.component';
import { StaffSalaryRoutingResolveService } from './staff-salary-routing-resolve.service';

const staffSalaryRoute: Routes = [
  {
    path: '',
    component: StaffSalaryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StaffSalaryDetailComponent,
    resolve: {
      staffSalary: StaffSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StaffSalaryUpdateComponent,
    resolve: {
      staffSalary: StaffSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StaffSalaryUpdateComponent,
    resolve: {
      staffSalary: StaffSalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(staffSalaryRoute)],
  exports: [RouterModule],
})
export class StaffSalaryRoutingModule {}
