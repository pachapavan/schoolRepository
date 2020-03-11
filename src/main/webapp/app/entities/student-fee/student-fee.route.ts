import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStudentFee, StudentFee } from 'app/shared/model/student-fee.model';
import { StudentFeeService } from './student-fee.service';
import { StudentFeeComponent } from './student-fee.component';
import { StudentFeeDetailComponent } from './student-fee-detail.component';
import { StudentFeeUpdateComponent } from './student-fee-update.component';

@Injectable({ providedIn: 'root' })
export class StudentFeeResolve implements Resolve<IStudentFee> {
  constructor(private service: StudentFeeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentFee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((studentFee: HttpResponse<StudentFee>) => {
          if (studentFee.body) {
            return of(studentFee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentFee());
  }
}

export const studentFeeRoute: Routes = [
  {
    path: '',
    component: StudentFeeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentFeeDetailComponent,
    resolve: {
      studentFee: StudentFeeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentFeeUpdateComponent,
    resolve: {
      studentFee: StudentFeeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentFeeUpdateComponent,
    resolve: {
      studentFee: StudentFeeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
