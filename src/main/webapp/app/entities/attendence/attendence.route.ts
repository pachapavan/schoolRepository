import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttendence, Attendence } from 'app/shared/model/attendence.model';
import { AttendenceService } from './attendence.service';
import { AttendenceComponent } from './attendence.component';
import { AttendenceDetailComponent } from './attendence-detail.component';
import { AttendenceUpdateComponent } from './attendence-update.component';

@Injectable({ providedIn: 'root' })
export class AttendenceResolve implements Resolve<IAttendence> {
  constructor(private service: AttendenceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attendence: HttpResponse<Attendence>) => {
          if (attendence.body) {
            return of(attendence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Attendence());
  }
}

export const attendenceRoute: Routes = [
  {
    path: '',
    component: AttendenceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.attendence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AttendenceDetailComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.attendence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.attendence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AttendenceUpdateComponent,
    resolve: {
      attendence: AttendenceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.attendence.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
