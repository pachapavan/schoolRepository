import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClassName, ClassName } from 'app/shared/model/class-name.model';
import { ClassNameService } from './class-name.service';
import { ClassNameComponent } from './class-name.component';
import { ClassNameDetailComponent } from './class-name-detail.component';
import { ClassNameUpdateComponent } from './class-name-update.component';

@Injectable({ providedIn: 'root' })
export class ClassNameResolve implements Resolve<IClassName> {
  constructor(private service: ClassNameService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((className: HttpResponse<ClassName>) => {
          if (className.body) {
            return of(className.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassName());
  }
}

export const classNameRoute: Routes = [
  {
    path: '',
    component: ClassNameComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.className.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClassNameDetailComponent,
    resolve: {
      className: ClassNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.className.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClassNameUpdateComponent,
    resolve: {
      className: ClassNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.className.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClassNameUpdateComponent,
    resolve: {
      className: ClassNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.className.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
