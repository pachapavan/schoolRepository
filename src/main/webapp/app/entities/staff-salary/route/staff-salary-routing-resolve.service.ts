import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStaffSalary, StaffSalary } from '../staff-salary.model';
import { StaffSalaryService } from '../service/staff-salary.service';

@Injectable({ providedIn: 'root' })
export class StaffSalaryRoutingResolveService implements Resolve<IStaffSalary> {
  constructor(protected service: StaffSalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStaffSalary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((staffSalary: HttpResponse<StaffSalary>) => {
          if (staffSalary.body) {
            return of(staffSalary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StaffSalary());
  }
}
