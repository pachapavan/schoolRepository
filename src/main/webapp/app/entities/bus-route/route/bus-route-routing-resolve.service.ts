import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusRoute, BusRoute } from '../bus-route.model';
import { BusRouteService } from '../service/bus-route.service';

@Injectable({ providedIn: 'root' })
export class BusRouteRoutingResolveService implements Resolve<IBusRoute> {
  constructor(protected service: BusRouteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusRoute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((busRoute: HttpResponse<BusRoute>) => {
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
