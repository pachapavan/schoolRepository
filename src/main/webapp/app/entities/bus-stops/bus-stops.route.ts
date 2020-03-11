import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBusStops, BusStops } from 'app/shared/model/bus-stops.model';
import { BusStopsService } from './bus-stops.service';
import { BusStopsComponent } from './bus-stops.component';
import { BusStopsDetailComponent } from './bus-stops-detail.component';
import { BusStopsUpdateComponent } from './bus-stops-update.component';

@Injectable({ providedIn: 'root' })
export class BusStopsResolve implements Resolve<IBusStops> {
  constructor(private service: BusStopsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusStops> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((busStops: HttpResponse<BusStops>) => {
          if (busStops.body) {
            return of(busStops.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusStops());
  }
}

export const busStopsRoute: Routes = [
  {
    path: '',
    component: BusStopsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busStops.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BusStopsDetailComponent,
    resolve: {
      busStops: BusStopsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busStops.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BusStopsUpdateComponent,
    resolve: {
      busStops: BusStopsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busStops.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BusStopsUpdateComponent,
    resolve: {
      busStops: BusStopsResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'jhipsterSampleApplicationApp.busStops.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
