import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusRouteName, BusRouteName } from '../bus-route-name.model';
import { BusRouteNameService } from '../service/bus-route-name.service';

@Injectable({ providedIn: 'root' })
export class BusRouteNameRoutingResolveService implements Resolve<IBusRouteName> {
  constructor(protected service: BusRouteNameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusRouteName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((busRouteName: HttpResponse<BusRouteName>) => {
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
