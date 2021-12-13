import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentFee, getStudentFeeIdentifier } from '../student-fee.model';

export type EntityResponseType = HttpResponse<IStudentFee>;
export type EntityArrayResponseType = HttpResponse<IStudentFee[]>;

@Injectable({ providedIn: 'root' })
export class StudentFeeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/student-fees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(studentFee: IStudentFee): Observable<EntityResponseType> {
    return this.http.post<IStudentFee>(this.resourceUrl, studentFee, { observe: 'response' });
  }

  update(studentFee: IStudentFee): Observable<EntityResponseType> {
    return this.http.put<IStudentFee>(`${this.resourceUrl}/${getStudentFeeIdentifier(studentFee) as number}`, studentFee, {
      observe: 'response',
    });
  }

  partialUpdate(studentFee: IStudentFee): Observable<EntityResponseType> {
    return this.http.patch<IStudentFee>(`${this.resourceUrl}/${getStudentFeeIdentifier(studentFee) as number}`, studentFee, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentFee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentFee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudentFeeToCollectionIfMissing(
    studentFeeCollection: IStudentFee[],
    ...studentFeesToCheck: (IStudentFee | null | undefined)[]
  ): IStudentFee[] {
    const studentFees: IStudentFee[] = studentFeesToCheck.filter(isPresent);
    if (studentFees.length > 0) {
      const studentFeeCollectionIdentifiers = studentFeeCollection.map(studentFeeItem => getStudentFeeIdentifier(studentFeeItem)!);
      const studentFeesToAdd = studentFees.filter(studentFeeItem => {
        const studentFeeIdentifier = getStudentFeeIdentifier(studentFeeItem);
        if (studentFeeIdentifier == null || studentFeeCollectionIdentifiers.includes(studentFeeIdentifier)) {
          return false;
        }
        studentFeeCollectionIdentifiers.push(studentFeeIdentifier);
        return true;
      });
      return [...studentFeesToAdd, ...studentFeeCollection];
    }
    return studentFeeCollection;
  }
}
