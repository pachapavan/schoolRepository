import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBusRoute, BusRoute } from 'app/shared/model/bus-route.model';
import { BusRouteService } from './bus-route.service';
import { BusRouteComponent } from './bus-route.component';
import { BusRouteDetailComponent } from './bus-route-detail.component';
import { BusRouteUpdateComponent } from './bus-route-update.component';

@Injectable({ providedIn: 'root' })
export class BusRouteResolve implements Resolve<IBusRoute> {
  constructor(private service: BusRouteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusRoute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((busRoute: HttpResponse<BusRoute>) => {
          if (busRoute.body) {
            return of(busRoute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusRoute());
  }
}

export const busRouteRoute: Routes = [
  {
    path: '',
    component: BusRouteComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRoute.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BusRouteDetailComponent,
    resolve: {
      busRoute: BusRouteResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRoute.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BusRouteUpdateComponent,
    resolve: {
      busRoute: BusRouteResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRoute.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BusRouteUpdateComponent,
    resolve: {
      busRoute: BusRouteResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busRoute.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
