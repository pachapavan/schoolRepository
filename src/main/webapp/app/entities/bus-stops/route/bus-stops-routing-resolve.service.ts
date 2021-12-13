import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusStops, BusStops } from '../bus-stops.model';
import { BusStopsService } from '../service/bus-stops.service';

@Injectable({ providedIn: 'root' })
export class BusStopsRoutingResolveService implements Resolve<IBusStops> {
  constructor(protected service: BusStopsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusStops> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((busStops: HttpResponse<BusStops>) => {
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
