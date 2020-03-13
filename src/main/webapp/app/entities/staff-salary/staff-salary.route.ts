import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStaffSalary, StaffSalary } from 'app/shared/model/staff-salary.model';
import { StaffSalaryService } from './staff-salary.service';
import { StaffSalaryComponent } from './staff-salary.component';
import { StaffSalaryDetailComponent } from './staff-salary-detail.component';
import { StaffSalaryUpdateComponent } from './staff-salary-update.component';

@Injectable({ providedIn: 'root' })
export class StaffSalaryResolve implements Resolve<IStaffSalary> {
  constructor(private service: StaffSalaryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStaffSalary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((staffSalary: HttpResponse<StaffSalary>) => {
          if (staffSalary.body) {
            return of(staffSalary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StaffSalary());
  }
}

export const staffSalaryRoute: Routes = [
  {
    path: '',
    component: StaffSalaryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.staffSalary.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StaffSalaryDetailComponent,
    resolve: {
      staffSalary: StaffSalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.staffSalary.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StaffSalaryUpdateComponent,
    resolve: {
      staffSalary: StaffSalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.staffSalary.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StaffSalaryUpdateComponent,
    resolve: {
      staffSalary: StaffSalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.staffSalary.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
