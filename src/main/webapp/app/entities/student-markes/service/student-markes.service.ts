import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentMarkes, getStudentMarkesIdentifier } from '../student-markes.model';

export type EntityResponseType = HttpResponse<IStudentMarkes>;
export type EntityArrayResponseType = HttpResponse<IStudentMarkes[]>;

@Injectable({ providedIn: 'root' })
export class StudentMarkesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/student-markes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(studentMarkes: IStudentMarkes): Observable<EntityResponseType> {
    return this.http.post<IStudentMarkes>(this.resourceUrl, studentMarkes, { observe: 'response' });
  }

  update(studentMarkes: IStudentMarkes): Observable<EntityResponseType> {
    return this.http.put<IStudentMarkes>(`${this.resourceUrl}/${getStudentMarkesIdentifier(studentMarkes) as number}`, studentMarkes, {
      observe: 'response',
    });
  }

  partialUpdate(studentMarkes: IStudentMarkes): Observable<EntityResponseType> {
    return this.http.patch<IStudentMarkes>(`${this.resourceUrl}/${getStudentMarkesIdentifier(studentMarkes) as number}`, studentMarkes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentMarkes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentMarkes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentMarkesToCollectionIfMissing(
    studentMarkesCollection: IStudentMarkes[],
    ...studentMarkesToCheck: (IStudentMarkes | null | undefined)[]
  ): IStudentMarkes[] {
    const studentMarkes: IStudentMarkes[] = studentMarkesToCheck.filter(isPresent);
    if (studentMarkes.length > 0) {
      const studentMarkesCollectionIdentifiers = studentMarkesCollection.map(
        studentMarkesItem => getStudentMarkesIdentifier(studentMarkesItem)!
      );
      const studentMarkesToAdd = studentMarkes.filter(studentMarkesItem => {
        const studentMarkesIdentifier = getStudentMarkesIdentifier(studentMarkesItem);
        if (studentMarkesIdentifier == null || studentMarkesCollectionIdentifiers.includes(studentMarkesIdentifier)) {
          return false;
        }
        studentMarkesCollectionIdentifiers.push(studentMarkesIdentifier);
        return true;
      });
      return [...studentMarkesToAdd, ...studentMarkesCollection];
    }
    return studentMarkesCollection;
  }
}
