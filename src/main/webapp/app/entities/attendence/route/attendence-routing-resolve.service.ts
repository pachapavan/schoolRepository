import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttendence, Attendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

@Injectable({ providedIn: 'root' })
export class AttendenceRoutingResolveService implements Resolve<IAttendence> {
  constructor(protected service: AttendenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttendence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((attendence: HttpResponse<Attendence>) => {
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
