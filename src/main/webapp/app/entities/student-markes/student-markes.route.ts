import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStudentMarkes, StudentMarkes } from 'app/shared/model/student-markes.model';
import { StudentMarkesService } from './student-markes.service';
import { StudentMarkesComponent } from './student-markes.component';
import { StudentMarkesDetailComponent } from './student-markes-detail.component';
import { StudentMarkesUpdateComponent } from './student-markes-update.component';

@Injectable({ providedIn: 'root' })
export class StudentMarkesResolve implements Resolve<IStudentMarkes> {
  constructor(private service: StudentMarkesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentMarkes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((studentMarkes: HttpResponse<StudentMarkes>) => {
          if (studentMarkes.body) {
            return of(studentMarkes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentMarkes());
  }
}

export const studentMarkesRoute: Routes = [
  {
    path: '',
    component: StudentMarkesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentMarkes.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentMarkesDetailComponent,
    resolve: {
      studentMarkes: StudentMarkesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentMarkes.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentMarkesUpdateComponent,
    resolve: {
      studentMarkes: StudentMarkesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentMarkes.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentMarkesUpdateComponent,
    resolve: {
      studentMarkes: StudentMarkesResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.studentMarkes.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
