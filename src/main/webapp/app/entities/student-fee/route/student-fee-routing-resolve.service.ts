import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentFee, StudentFee } from '../student-fee.model';
import { StudentFeeService } from '../service/student-fee.service';

@Injectable({ providedIn: 'root' })
export class StudentFeeRoutingResolveService implements Resolve<IStudentFee> {
  constructor(protected service: StudentFeeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudentFee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((studentFee: HttpResponse<StudentFee>) => {
          if (studentFee.body) {
            return of(studentFee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StudentFee());
  }
}
