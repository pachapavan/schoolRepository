import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassName, getClassNameIdentifier } from '../class-name.model';

export type EntityResponseType = HttpResponse<IClassName>;
export type EntityArrayResponseType = HttpResponse<IClassName[]>;

@Injectable({ providedIn: 'root' })
export class ClassNameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/class-names');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(className: IClassName): Observable<EntityResponseType> {
    return this.http.post<IClassName>(this.resourceUrl, className, { observe: 'response' });
  }

  update(className: IClassName): Observable<EntityResponseType> {
    return this.http.put<IClassName>(`${this.resourceUrl}/${getClassNameIdentifier(className) as number}`, className, {
      observe: 'response',
    });
  }

  partialUpdate(className: IClassName): Observable<EntityResponseType> {
    return this.http.patch<IClassName>(`${this.resourceUrl}/${getClassNameIdentifier(className) as number}`, className, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassName>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassName[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassNameToCollectionIfMissing(
    classNameCollection: IClassName[],
    ...classNamesToCheck: (IClassName | null | undefined)[]
  ): IClassName[] {
    const classNames: IClassName[] = classNamesToCheck.filter(isPresent);
    if (classNames.length > 0) {
      const classNameCollectionIdentifiers = classNameCollection.map(classNameItem => getClassNameIdentifier(classNameItem)!);
      const classNamesToAdd = classNames.filter(classNameItem => {
        const classNameIdentifier = getClassNameIdentifier(classNameItem);
        if (classNameIdentifier == null || classNameCollectionIdentifiers.includes(classNameIdentifier)) {
          return false;
        }
        classNameCollectionIdentifiers.push(classNameIdentifier);
        return true;
      });
      return [...classNamesToAdd, ...classNameCollection];
    }
    return classNameCollection;
  }
}
