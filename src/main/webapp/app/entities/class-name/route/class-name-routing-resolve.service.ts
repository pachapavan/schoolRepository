import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassName, ClassName } from '../class-name.model';
import { ClassNameService } from '../service/class-name.service';

@Injectable({ providedIn: 'root' })
export class ClassNameRoutingResolveService implements Resolve<IClassName> {
  constructor(protected service: ClassNameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassName> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((className: HttpResponse<ClassName>) => {
          if (className.body) {
            return of(className.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassName());
  }
}
