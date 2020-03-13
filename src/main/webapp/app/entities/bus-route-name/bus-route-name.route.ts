import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBusRouteName, BusRouteName } from 'app/shared/model/bus-route-name.model';
import { BusRouteNameService } from './bus-route-name.service';
import { BusRouteNameComponent } from './bus-route-name.component';
import { BusRouteNameDetailComponent } from './bus-route-name-detail.component';
import { BusRouteNameUpdateComponent } from './bus-route-name-update.component';

@Injectable({ providedIn: 'root' })
export class BusRouteNameResolve implements Resolve<IBusRouteName> {
  constructor(private service: BusRouteNameService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusRouteName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((busRouteName: HttpResponse<BusRouteName>) => {
          if (busRouteName.body) {
            return of(busRouteName.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusRouteName());
  }
}

export const busRouteNameRoute: Routes = [
  {
    path: '',
    component: BusRouteNameComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRouteName.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BusRouteNameDetailComponent,
    resolve: {
      busRouteName: BusRouteNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRouteName.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BusRouteNameUpdateComponent,
    resolve: {
      busRouteName: BusRouteNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRouteName.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BusRouteNameUpdateComponent,
    resolve: {
      busRouteName: BusRouteNameResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRouteName.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
