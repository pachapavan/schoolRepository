import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentMarkes, StudentMarkes } from '../student-markes.model';
import { StudentMarkesService } from '../service/student-markes.service';

@Injectable({ providedIn: 'root' })
export class StudentMarkesRoutingResolveService implements Resolve<IStudentMarkes> {
  constructor(protected service: StudentMarkesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentMarkes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentMarkes: HttpResponse<StudentMarkes>) => {
          if (studentMarkes.body) {
            return of(studentMarkes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentMarkes());
  }
}
